package com.lxkplus.RandomInit.service;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface StatementService {
    MySqlCreateTableStatement readCreateSql(String CreateSql) throws NormalErrorException;
    String statementToCreateDDL(MySqlCreateTableStatement statement) throws NormalErrorException;
}
