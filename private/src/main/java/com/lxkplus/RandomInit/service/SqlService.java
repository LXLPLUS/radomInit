package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface SqlService {

    void fillRandomData(String actionID, String databaseName, String tableName, int count) throws NormalErrorException;

    String selectFill(String selectSql, String createSql) throws NormalErrorException;
}
