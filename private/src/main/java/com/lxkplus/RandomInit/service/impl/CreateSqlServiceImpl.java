package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.service.CreateSqlService;
import com.lxkplus.RandomInit.service.RandomService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateSqlServiceImpl implements CreateSqlService {

    @Resource
    RandomService randomService;

    @Override
    public MySqlCreateTableStatement readCreateSql(String sql) throws NormalErrorException {
        ThrowUtils.throwIfNullOrBlack(sql, "sql数据为空");
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        ThrowUtils.throwIfTypeError(sqlStatement, MySqlCreateTableStatement.class, "类型不是建表语句！");
        return (MySqlCreateTableStatement) sqlStatement;
    }

    @Override
    public String statementToCreateDDL(MySqlCreateTableStatement statement) throws NormalErrorException {
        ThrowUtils.throwIfNull(statement, "解析sql空指针异常");
        return statement.toString();
    }

    @Override
    public List<String> createToInsert(String sql, int count) throws NormalErrorException {
        MySqlCreateTableStatement statement = readCreateSql(sql);
        ThrowUtils.throwIf(count <= 0, ErrorEnum.paramNotSupport, "数据规模为非正数");

        MySqlInsertStatement mySqlInsertStatement = new MySqlInsertStatement();
        // 表名
        mySqlInsertStatement.setTableName(statement.getName());
        for (SQLTableElement sqlColumn : statement.getTableElementList()) {
            if (sqlColumn instanceof SQLColumnDefinition dataRow) {
                // 写入列名
                SQLName name = dataRow.getName();
                SQLIdentifierExpr sqlIdentifierExpr = new SQLIdentifierExpr(name.getSimpleName());
                mySqlInsertStatement.addColumn(sqlIdentifierExpr);
            }
        }

        List<String> sqlList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SQLInsertStatement.ValuesClause valuesClause = new SQLInsertStatement.ValuesClause();
            for (SQLTableElement sqlColumn : statement.getTableElementList()) {
                if (sqlColumn instanceof SQLColumnDefinition dataRow) {
                    String name = dataRow.getDataType().getName();
                    List<SQLExpr> arguments = dataRow.getDataType().getArguments();
                    Object dataByRow = randomService.randomByColumn(dataRow);
                    Object dataByType = randomService.randomByType(name, arguments);
                    valuesClause.addValue(dataByRow != null ? dataByRow: dataByType);
                }
            }
            mySqlInsertStatement.setValues(valuesClause);
            sqlList.add(mySqlInsertStatement + ";");
        }
    return sqlList;
    }

    @Override
    public String selectFill(String selectSql, String createSql) throws NormalErrorException {
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(selectSql);
        ThrowUtils.throwIfTypeError(sqlStatement, SQLSelectStatement.class, String.format("%s不是select语句", selectSql));
        SQLSelectQueryBlock queryBlock = ((SQLSelectStatement) sqlStatement).getSelect().getQueryBlock();

        // 检查是否存在 * 字符
        if (queryBlock.selectItemHasAllColumn()) {
            MySqlCreateTableStatement statement = readCreateSql(createSql);

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
