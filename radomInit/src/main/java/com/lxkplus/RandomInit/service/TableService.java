package com.lxkplus.RandomInit.service;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.lxkplus.RandomInit.dto.TableParams;
import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface TableService {

    MySqlCreateTableStatement convertVoToStatement(TableParams tableParams);

    TableParams convertStatToVo(String sql) throws NormalErrorException;

    void createTable(String actionID, String databaseName, String sql) throws NormalErrorException;

    String getTableDDL(String actionID, String databaseName, String tableName) throws NormalErrorException;
}
