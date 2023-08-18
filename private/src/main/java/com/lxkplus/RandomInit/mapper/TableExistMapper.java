package com.lxkplus.RandomInit.mapper;

import com.lxkplus.RandomInit.model.MysqlColumn;
import com.lxkplus.RandomInit.model.MysqlTable;

import java.util.List;

public interface TableExistMapper {

    List<MysqlTable> getUserDatabase(String actionID, String userDatabaseName);

    int getUserDatabaseCount(String actionID, String userDatabaseName);

    List<MysqlTable> getTable(String actionID, String userDatabaseName, String tableName);

    int getTableCount(String actionID, String userDatabaseName, String tableName);

    List<MysqlColumn> getColumn(String actionID, String userDatabaseName, String tableName, String columnName);

    int getColumnCount(String actionID, String userDatabaseName, String tableName, String columnName);
}
