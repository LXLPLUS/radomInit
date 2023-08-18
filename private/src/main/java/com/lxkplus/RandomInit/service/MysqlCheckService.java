package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface MysqlCheckService {
    void checkTableNameSafe(String actionID, String userDatabaseName, String tableName) throws NormalErrorException;

    void checkActionIDSafe(String actionID) throws NormalErrorException;

    void checkDataBaseNameSafe(String userDatabaseName) throws NormalErrorException;

    void checkTableNameSafe(String tableName) throws NormalErrorException;

    boolean checkDatabaseExist(String actionID, String userDatabaseName, boolean strict) throws NormalErrorException;

    boolean checkTableExist(String actionID, String userDatabaseName, String tableName, boolean strict) throws NormalErrorException;

    boolean checkColumnExist(String actionID, String userDatabaseName, String tableName, String column, boolean strict) throws NormalErrorException;
}
