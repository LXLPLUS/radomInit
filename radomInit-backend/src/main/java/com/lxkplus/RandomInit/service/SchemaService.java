package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public interface SchemaService {
    String getRealSchemaName(String actionID, String databaseName);

    /**
     * 非暴露接口，是给table操作进行解耦用
     * 不可暴露给用户
     * @param databaseName  数据库真实名称
     * @return {actionID, 前端的数据库名称}
     */
    Pair<String, String> getFromRealSchemaName(String databaseName) throws NormalErrorException;

    String createSchema(String actionID, String databaseID) throws NormalErrorException;

    String createSchema(String actionId) throws NormalErrorException;

    List<Map<String, String>> dropSchemaByID(String actionID) throws NormalErrorException;

    List<Map<String, String>> dropAllSchema();

    List<Map<String, String>> dropSchemaByName(String actionID, String databaseName) throws NormalErrorException;
}
