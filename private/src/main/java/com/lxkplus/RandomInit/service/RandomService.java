package com.lxkplus.RandomInit.service;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.ColumnInfo;

import java.util.List;

public interface RandomService {

    boolean registerRuler(String actionID, String userDatabaseName,
                          String tableName, String column, String builderRuler,
                          List<String> value) throws NormalErrorException;

    boolean registerRuler(String actionID, String userDatabaseName,
                          String tableName, String column, String builderRuler, String params) throws NormalErrorException;

    boolean registerRuler(String actionID, String userDatabaseName,
                          String tableName, String column, String builderRuler) throws NormalErrorException;

    ColumnInfo readRuler(String actionID, String databaseName, String tableName, String column);

    List<ColumnInfo> getRulerList(String actionID) throws NormalErrorException;


    void clearRulers(String actionID);

    Object randomByRuler(String buildRuler, List<String> params) throws NormalErrorException;

    Object randomByRuler(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException;

    Object randomByType(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException;

    Object randomByColumn(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement);
}
