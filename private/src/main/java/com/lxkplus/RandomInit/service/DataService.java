package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

import java.util.LinkedHashMap;
import java.util.List;

public interface DataService {

    void fillRandomData(String actionID, String userDatabaseName, String tableName, int count) throws NormalErrorException;

    String selectFill(String selectSql, String createSql) throws NormalErrorException;

    List<LinkedHashMap<String, String>> getAllDataMap(String actionID, String userDatabaseName, String tableName);

    List<List<String>> getAllDataList(String actionID, String userDatabaseName, String tableName, boolean headExist);

    List<List<String>> getAllDataList(String actionID, String userDatabaseName, String tableName);
}
