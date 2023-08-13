package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

import java.util.List;

public interface TableService {
    void checkTable(String actionID, String databaseName, String tableName) throws NormalErrorException;

    boolean checkTableExist(String actionID, String databaseName, String tableName) throws NormalErrorException;

    List<String> getTableList(String actionID, String databaseName, String tableName) throws NormalErrorException;

    List<String> getTableList(String actionID, String databaseName);

    void createTable(String actionID, String databaseName, String sql) throws NormalErrorException;
}
