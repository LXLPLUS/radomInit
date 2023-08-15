package com.lxkplus.RandomInit.service;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface RandomService {

    Object randomByType(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException;

    Object randomByColumn(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement);
}
