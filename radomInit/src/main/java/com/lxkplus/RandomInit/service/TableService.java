package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface TableService {

    void createTable(String actionID, String databaseName, String sql) throws NormalErrorException;

    int clearData(String actionID, String databaseName, String tableName) throws NormalErrorException;

    String getTableDDL(String actionID, String databaseName, String tableName) throws NormalErrorException;
}
