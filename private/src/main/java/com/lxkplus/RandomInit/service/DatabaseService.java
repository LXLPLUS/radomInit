package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public interface DatabaseService {
    String getRealDatabaseName(String actionID, String databaseName);

    /**
     * 非暴露接口，是给table操作进行解耦用
     * 不可暴露给用户
     * @param databaseName  数据库真实名称
     * @return 所有的数据库列表
     */
    Pair<String, String> getFromRealDatabaseName(String databaseName) throws NormalErrorException;

    void checkID(String actionID) throws NormalErrorException;

    void checkIDAndName(String actionID, String databaseName) throws NormalErrorException;

    List<String> getDatabaseByID(String actionID) throws NormalErrorException;

    boolean checkDatabaseExist(String actionID, String name) throws NormalErrorException;

    String createDatabase(String actionID, String databaseID) throws NormalErrorException;

    String createDatabase(String actionId) throws NormalErrorException;

    List<Map<String, String>> dropDatabaseByID(String actionID) throws NormalErrorException;

    List<Map<String, String>> dropAllDatabase();

    List<Map<String, String>> dropDatabaseByName(String actionID, String databaseName) throws NormalErrorException;
}
