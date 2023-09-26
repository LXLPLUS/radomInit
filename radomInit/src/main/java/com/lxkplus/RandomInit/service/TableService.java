package com.lxkplus.RandomInit.service;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.VO.TableParams;

public interface TableService {

    MySqlCreateTableStatement convertVoToStatement(TableParams tableParams);

    TableParams convertStatToVo(String sql) throws NormalErrorException;

    void createTable(String actionID, String databaseName, String sql) throws NormalErrorException;

    int clearData(String actionID, String databaseName, String tableName) throws NormalErrorException;

    String getTableDDL(String actionID, String databaseName, String tableName) throws NormalErrorException;
}
