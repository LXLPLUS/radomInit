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
import com.lxkplus.RandomInit.mapper.TableMapper;
import com.lxkplus.RandomInit.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SqlServiceImpl implements SqlService {

    @Resource
    RandomService randomService;

    @Resource
    DatabaseService databaseService;

    @Resource
    StatementService createSqlService;

    @Resource
    StatementService statementService;

    @Resource
    TableService tableService;

    @Resource
    TableMapper tableMapper;

    @Override
    public void fillRandomData(String actionID, String databaseName, String tableName, int count) throws NormalErrorException {

        ThrowUtils.throwIf(!tableService.checkTableExist(actionID, databaseName, tableName),
                ErrorEnum.Empty,
                "对应的数据库表不存在");

        String realDatabaseName = databaseService.getRealDatabaseName(actionID, databaseName);

        String createDDL = tableService.getTableDDL(actionID, databaseName, tableName);
        MySqlCreateTableStatement statement = statementService.readCreateSql(createDDL);
        ThrowUtils.throwIf(count <= 0, ErrorEnum.paramNotSupport, "数据规模为非正数");

        // 将建表语句转化为sql
        MySqlInsertStatement mySqlInsertStatement = new MySqlInsertStatement();

        // 拦截sql并引导到另外一个数据库
        mySqlInsertStatement.setTableName(statement.getName());
        mySqlInsertStatement.getTableSource().setSchema(realDatabaseName);

        for (SQLTableElement sqlColumn : statement.getTableElementList()) {
            if (sqlColumn instanceof SQLColumnDefinition dataRow) {
                // 写入列名
                SQLName name = dataRow.getName();
                SQLIdentifierExpr sqlIdentifierExpr = new SQLIdentifierExpr(name.getSimpleName());
                mySqlInsertStatement.addColumn(sqlIdentifierExpr);
            }
        }

        for (int i = 0; i < count; i++) {
            SQLInsertStatement.ValuesClause valuesClause = new SQLInsertStatement.ValuesClause();
            for (SQLTableElement sqlColumn : statement.getTableElementList()) {
                if (sqlColumn instanceof SQLColumnDefinition dataRow) {
                    Object dataByRow = randomService.randomByColumn(actionID, dataRow, mySqlInsertStatement);
                    if (dataByRow == null) {
                        dataByRow = randomService.randomByType(actionID, dataRow, mySqlInsertStatement);
                    }
                    valuesClause.addValue(dataByRow);
                }
            }
            mySqlInsertStatement.setValues(valuesClause);
            tableMapper.FillRandomData(mySqlInsertStatement.toString());
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

}
