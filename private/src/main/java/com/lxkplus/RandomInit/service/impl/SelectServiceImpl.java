package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.model.DatabaseNameHelper;
import com.lxkplus.RandomInit.service.DatabaseService;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import com.lxkplus.RandomInit.service.SelectService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SelectServiceImpl implements SelectService {

    @Resource
    DatabaseService databaseService;

    @Resource
    MysqlCheckService mysqlCheckService;

    @Override
    public SQLSelectStatement getSelect(String selectSql) throws NormalErrorException {
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(selectSql);
        ThrowUtils.throwIfTypeError(sqlStatement, SQLSelectStatement.class, "不是查询语句！");
        return (SQLSelectStatement) sqlStatement;
    }

    @Override
    public String SqlConvert(String selectSql,
                             String actionID, String activeDatabaseName, boolean strict) throws NormalErrorException {
        SQLSelectStatement select = getSelect(selectSql);
        List<DatabaseNameHelper> nameFromSelect = getNameFromSelect(select);

        for (DatabaseNameHelper databaseNameHelper : nameFromSelect) {
            String userDatabaseName = databaseNameHelper.getUserDatabaseName();
            if (databaseNameHelper.getTableName() != null && strict) {
                userDatabaseName = StringUtils.defaultIfBlank(userDatabaseName, activeDatabaseName);
                ThrowUtils.throwIf(
                        !mysqlCheckService.checkTableExist(actionID,
                                userDatabaseName,
                                databaseNameHelper.getTableName(),
                                false),
                        ErrorEnum.NotExist,
                        "对应的数据库" + userDatabaseName + "." + databaseNameHelper.getTableName() + "不存在");
            }
        }
        select = changeSelect(select, actionID, activeDatabaseName, nameFromSelect);
        return select.toString();
    }

    @Override
    public String SqlConvert(String selectSql,
                             String actionID, String activeDatabaseName) throws NormalErrorException {
        return SqlConvert(selectSql, actionID, activeDatabaseName, true);
    }

    @Override
    public List<DatabaseNameHelper> getNameFromSelect(SQLSelectStatement statement) {
        List<DatabaseNameHelper> nameList = new ArrayList<>();
        SQLSelectQuery query = statement.getSelect().getQuery();
        getNameFromQuery(query, nameList);
        return nameList;
    }

    private void getNameFromQuery(SQLSelectQuery query, List<DatabaseNameHelper> nameList) {
        // 检查单表
        if (query instanceof MySqlSelectQueryBlock queryBlock) {
            getNameFromSource(queryBlock.getFrom(), nameList);
        }
        // 检查union
        else if (query instanceof SQLUnionQuery unionQuery) {
            for (SQLSelectQuery relation : unionQuery.getRelations()) {
                getNameFromQuery(relation, nameList);
            }
        }
    }

    private void getNameFromSource(SQLTableSource sqlTableSource, List<DatabaseNameHelper> nameList) {
        // 检查join的表名
        if (sqlTableSource instanceof SQLJoinTableSource source) {
            getNameFromSource(source.getLeft(), nameList);
            getNameFromSource(source.getRight(), nameList);
        }
        // 检查单表表名
        else if (sqlTableSource instanceof SQLExprTableSource source) {
            nameList.add(new DatabaseNameHelper(source.getSchema(), source.getTableName(), source.getAlias(), null));
        }
        // 检查临时表
        else if (sqlTableSource instanceof SQLSubqueryTableSource source) {
            nameList.add(new DatabaseNameHelper(null, null,
                    source.getAlias(), source.getSelect().toString().replaceAll("\n", "")));
            SQLSelectQuery query = source.getSelect().getQuery();
            getNameFromQuery(query, nameList);
        }
    }

    @Override
    public SQLSelectStatement changeSelect(
            SQLSelectStatement statement,
            String actionID,
            String activeDatabaseName,
            List<DatabaseNameHelper> nameList) throws NormalErrorException {

        SQLSelectQuery query = statement.getSelect().getQuery();
        changeQuery(query, actionID, activeDatabaseName, nameList);
        return statement;
    }

    private void changeQuery(SQLSelectQuery query,
                             String actionID,
                             String activeDatabaseName,
                             List<DatabaseNameHelper> nameList) throws NormalErrorException {
        // 检查单表
        if (query instanceof MySqlSelectQueryBlock queryBlock) {
            // 替换列名中的数据库
            for (SQLSelectItem sqlSelectItem : queryBlock.getSelectList()) {
                // 不存在归属则不动
                if (sqlSelectItem.getExpr() instanceof SQLPropertyExpr sqlPropertyExpr) {
                    if (sqlPropertyExpr.getOwner() == null) {
                        continue;
                    }
                    // 检查owner
                    String ownerName = sqlPropertyExpr.getOwner().toString();
                    for (DatabaseNameHelper databaseNameHelper : nameList) {
                        if (databaseNameHelper.getOwner().equals(ownerName)) {
                            String shame = StringUtils.defaultIfBlank(databaseNameHelper.getUserDatabaseName(),
                                    activeDatabaseName);
                            String realDatabaseName = databaseService.getRealDatabaseName(actionID, shame);
                            sqlPropertyExpr.setOwner(realDatabaseName + "." + databaseNameHelper.getTableName());
                        }
                        else if (databaseNameHelper.getAlias() != null && databaseNameHelper.getAlias().equals(ownerName)) {
                            break;
                        }
                    }
                }
            }
            changeSource(queryBlock.getFrom(), actionID, activeDatabaseName, nameList);
        }
        // 检查union
        else if (query instanceof SQLUnionQuery unionQuery) {
            for (SQLSelectQuery relation : unionQuery.getRelations()) {
                changeQuery(relation, actionID, activeDatabaseName, nameList);
            }
        }
    }

    private void changeSource(SQLTableSource sqlTableSource,
                              String actionID,
                              String activeDatabaseName,
                              List<DatabaseNameHelper> nameList) throws NormalErrorException {
        // 检查join的表名
        if (sqlTableSource instanceof SQLJoinTableSource source) {
            changeSource(source.getLeft(), actionID, activeDatabaseName, nameList);
            changeSource(source.getRight(), actionID, activeDatabaseName, nameList);

            if (source.getCondition() instanceof SQLBinaryOpExpr joinExpr) {
                if (joinExpr.getLeft() instanceof SQLPropertyExpr leftJoinExpr)
                    changeOwner(actionID, activeDatabaseName, nameList, leftJoinExpr);
                if (joinExpr.getRight() instanceof SQLPropertyExpr rightJoinExpr) {
                    changeOwner(actionID, activeDatabaseName, nameList, rightJoinExpr);
                }
            }
        }
        // 检查单表
        else if (sqlTableSource instanceof SQLExprTableSource source) {
            String schema = source.getSchema();
            schema = StringUtils.defaultIfBlank(schema, activeDatabaseName);
            String realDatabaseName = databaseService.getRealDatabaseName(actionID, schema);
            source.setSchema(realDatabaseName);
        }
        // 检查临时表
        else if (sqlTableSource instanceof SQLSubqueryTableSource source) {
            SQLSelectQuery query = source.getSelect().getQuery();
            changeQuery(query, actionID, activeDatabaseName, nameList);
        }
    }

    private void changeOwner(String actionID,
                             String activeDatabaseName,
                             List<DatabaseNameHelper> nameList,
                             SQLPropertyExpr leftJoinExpr) throws NormalErrorException {
        String owner = leftJoinExpr.getOwnerName();
        // 检查是否有匹配的名字，如果是表名就替换掉，如果是别名就保留
        for (DatabaseNameHelper databaseNameHelper : nameList) {
            if (databaseNameHelper.getOwner().equals(owner)) {
                String userDatabaseName = StringUtils.defaultIfBlank(databaseNameHelper.getUserDatabaseName(), activeDatabaseName);
                String realDatabaseName = databaseService.getRealDatabaseName(actionID, userDatabaseName);
                leftJoinExpr.setOwner(realDatabaseName + "." + databaseNameHelper.getTableName());
                return;
            }
            else if (owner.equals(databaseNameHelper.getAlias())) {
                return;
            }
        }
        throw new NormalErrorException(ErrorEnum.paramNotSupport, "参数" + owner + "不存在");
    }
}
