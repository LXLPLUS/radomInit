package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface MysqlCheckService {
    void checkTableNameSafe(String actionID, String userSchemaName, String tableName) throws NormalErrorException;

    void checkActionIDSafe(String actionID) throws NormalErrorException;

    void checkDataBaseNameSafe(String userSchemaName) throws NormalErrorException;

    void checkTableNameSafe(String tableName) throws NormalErrorException;

    boolean checkDatabaseExist(String actionID, String userSchemaName, boolean strict) throws NormalErrorException;

    boolean checkTableExist(String actionID, String userSchemaName, String tableName, boolean strict) throws NormalErrorException;

    boolean checkColumnExist(String actionID, String userSchemaName, String tableName, String column, boolean strict) throws NormalErrorException;
}
