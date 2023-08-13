package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public interface DatabaseService {
    String getRealDatabaseName(String actionID, String name);

    Pair<String, String> getParams(String databaseName) throws NormalErrorException;

    void checkID(String actionID) throws NormalErrorException;

    void checkIDAndName(String actionID, String databaseName) throws NormalErrorException;

    List<String> getDatabaseByID(String actionId) throws NormalErrorException;

    boolean checkDatabaseExist(String actionID, String name) throws NormalErrorException;

    String createDatabase(String actionID, String name) throws NormalErrorException;

    String createDatabase(String actionId) throws NormalErrorException;

    List<Map<String, String>> dropDatabaseByID(String actionID) throws NormalErrorException;

    List<Map<String, String>> dropAllDatabase();

    List<Map<String, String>> dropDatabaseByName(String actionID, String name) throws NormalErrorException;
}
