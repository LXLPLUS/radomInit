package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.TableActionMapper;
import com.lxkplus.RandomInit.model.DBModel.ColumnBuilderInfo;
import com.lxkplus.RandomInit.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

    @Resource
    RandomService randomService;

    @Resource
    SchemaService schemaService;

    @Resource
    StatementService createSqlService;

    @Resource
    StatementService statementService;

    @Resource
    TableService tableService;

    @Resource
    TableActionMapper tableMapper;

    @Resource
    MysqlCheckService mysqlCheckService;

    @Override
    public void fillRandomData(String actionID, String userSchemaName, String tableName, int count) throws NormalErrorException {

        ThrowUtils.throwIf(!mysqlCheckService.checkTableExist(actionID, userSchemaName, tableName, false),
                ErrorEnum.Empty,
                "对应的数据库表不存在");
        ThrowUtils.throwIf(count <= 0, ErrorEnum.paramNotSupport, "数据规模为非正数");

        String realSchemaName = schemaService.getRealSchemaName(actionID, userSchemaName);

        String createDDL = tableService.getTableDDL(actionID, userSchemaName, tableName);
        MySqlCreateTableStatement statement = statementService.readCreateSql(createDDL);

        // 将建表语句转化为sql
        MySqlInsertStatement mySqlInsertStatement = new MySqlInsertStatement();

        // 拦截sql并引导到另外一个数据库
        mySqlInsertStatement.setTableName(statement.getName());
        mySqlInsertStatement.getTableSource().setSchema(realSchemaName);

        for (SQLTableElement sqlColumn : statement.getTableElementList()) {
            if (sqlColumn instanceof SQLColumnDefinition dataRow) {
                // 写入列名
                SQLName name = dataRow.getName();
                SQLIdentifierExpr sqlIdentifierExpr = new SQLIdentifierExpr(name.getSimpleName());
                mySqlInsertStatement.addColumn(sqlIdentifierExpr);
            }
        }

        // 获取已经注册的规则
        List<ColumnBuilderInfo> rulerList = randomService.getRulerList(actionID);

        for (int i = 0; i < count; i++) {
            SQLInsertStatement.ValuesClause valuesClause = new SQLInsertStatement.ValuesClause();
            for (SQLTableElement sqlColumn : statement.getTableElementList()) {
                if (sqlColumn instanceof SQLColumnDefinition dataRow) {
                    // 从池里面获取 + 按照规则获取
                    Object dataFill = randomService.randomByRuler(actionID, dataRow, mySqlInsertStatement,
                            rulerList);
                    // 如果没法获取按照标准规则获取
                    if (dataFill == null) {
                        dataFill = randomService.randomByColumn(actionID, dataRow, mySqlInsertStatement);
                    }
                    // 按照格式类型获取
                    if (dataFill == null) {
                        dataFill = randomService.randomByType(actionID, dataRow, mySqlInsertStatement);
                    }
                    valuesClause.addValue(dataFill);
                }
            }
            mySqlInsertStatement.setValues(valuesClause);
            // 数据量太大，不全部打印
            if (i % 100 == 0) {
                log.info("写入{}: 第 {} 行数据, 数据为 {}", tableName, i, mySqlInsertStatement);
            }
            if (i == count - 1) {
                log.info("写入{} {}条完成！", tableName, count);
            }
            tableMapper.fillRandomData(mySqlInsertStatement.toString());
        }
    }

    @Override
    public String selectFill(String selectSql, String createSql) throws NormalErrorException {
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(selectSql);
        ThrowUtils.throwIfTypeError(sqlStatement, SQLSelectStatement.class, String.format("%s不是select语句", selectSql));
        SQLSelectQueryBlock queryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();

        // 检查是否存在 * 字符
        if (queryBlock.selectItemHasAllColumn()) {
            MySqlCreateTableStatement statement = createSqlService.readCreateSql(createSql);

            // 清空列
            List<SQLSelectItem> selectList = queryBlock.getSelectList();
            selectList.clear();
            // 获取建表语句的列名
            List<String> collect = statement.getColumnDefinitions().stream()
                    .map(x -> x.getName().getSimpleName())
                    .collect(Collectors.toList());
            // 将字段替换掉
            for (String sqlName : collect) {
                SQLSelectItem sqlSelectItem = new SQLSelectItem(new SQLIdentifierExpr(sqlName));
                queryBlock.addSelectItem(sqlSelectItem);
            }
        }

        return sqlStatement.toString();
    }

    @Override
    public List<LinkedHashMap<String, String>> getAllDataMap(String actionID, String userSchemaName, String tableName) {
        String realSchemaName = schemaService.getRealSchemaName(actionID, userSchemaName);

        return tableMapper.getData(realSchemaName, tableName);
    }

    @Override
    public List<List<String>> getAllDataList(String actionID, String userSchemaName, String tableName, boolean headExist) {
        List<LinkedHashMap<String, String>> allDataMap = getAllDataMap(actionID, userSchemaName, tableName);
        if (allDataMap == null || allDataMap.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<String>> dataList = new ArrayList<>();
        if (headExist) {
            dataList.add(new ArrayList<>(allDataMap.get(0).keySet()));
        }

        allDataMap.forEach(x -> dataList.add(new ArrayList<>(x.values())));
        return dataList;
    }

    @Override
    public List<List<String>> getAllDataList(String actionID, String userSchemaName, String tableName) {
        return getAllDataList(actionID, userSchemaName, tableName, true);
    }

}
