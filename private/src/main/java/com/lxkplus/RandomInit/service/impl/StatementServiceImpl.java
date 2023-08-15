package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.service.StatementService;
import org.springframework.stereotype.Service;

@Service
public class StatementServiceImpl implements StatementService {

    @Override
    public MySqlCreateTableStatement readCreateSql(String createSql) throws NormalErrorException {
        ThrowUtils.throwIfNullOrBlack(createSql, "sql数据为空");
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(createSql);
        ThrowUtils.throwIfTypeError(sqlStatement, MySqlCreateTableStatement.class, "类型不是建表语句！");
        return (MySqlCreateTableStatement) sqlStatement;
    }

    @Override
    public String statementToCreateDDL(MySqlCreateTableStatement statement) throws NormalErrorException {
        ThrowUtils.throwIfNull(statement, "解析sql空指针异常");
        return statement.toString();
    }
}
