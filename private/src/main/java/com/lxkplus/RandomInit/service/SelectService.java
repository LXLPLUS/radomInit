package com.lxkplus.RandomInit.service;

import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.DatabaseNameHelper;

import java.util.List;

public interface SelectService {
    SQLSelectStatement getSelect(String selectSql) throws NormalErrorException;

    String SqlConvert(String selectSql, String actionID, String activeDatabaseName) throws NormalErrorException;

    String SqlConvert(String selectSql,
                      String actionID, String activeDatabaseName, boolean strict) throws NormalErrorException;

    List<DatabaseNameHelper> getNameFromSelect(SQLSelectStatement statement);

    SQLSelectStatement changeSelect(
            SQLSelectStatement statement,
            String actionID,
            String activeDatabaseName,
            List<DatabaseNameHelper> nameList) throws NormalErrorException;
}
