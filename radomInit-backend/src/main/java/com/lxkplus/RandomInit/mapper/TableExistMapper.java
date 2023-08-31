package com.lxkplus.RandomInit.mapper;

import com.lxkplus.RandomInit.model.MysqlColumn;
import com.lxkplus.RandomInit.model.MysqlSchema;
import com.lxkplus.RandomInit.model.MysqlTable;

import java.util.List;

public interface TableExistMapper {

    List<MysqlSchema> getUserDatabase(String actionID, String userSchemaName);

    int getUserDatabaseCount(String actionID, String userSchemaName);

    List<MysqlTable> getTable(String actionID, String userSchemaName, String tableName);

    int getTableCount(String actionID, String userSchemaName, String tableName);

    List<MysqlColumn> getColumn(String actionID, String userSchemaName, String tableName, String columnName);

    int getColumnCount(String actionID, String userSchemaName, String tableName, String columnName);
}
