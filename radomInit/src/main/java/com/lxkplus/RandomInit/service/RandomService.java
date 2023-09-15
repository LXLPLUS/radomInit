package com.lxkplus.RandomInit.service;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.DO.ColumnBuilderInfo;
import com.lxkplus.RandomInit.model.VO.BuildRuler;
import com.lxkplus.RandomInit.model.VO.RegisterRulerVo;
import com.lxkplus.RandomInit.model.VO.SelectOption;

import java.util.List;

public interface RandomService {

    boolean registerRuler(String actionID, String userSchemaName,
                          String tableName, String column, String builderRuler,
                          List<String> value) throws NormalErrorException;

    boolean registerRuler(String actionID, String userSchemaName,
                          String tableName, String column, String builderRuler, String params) throws NormalErrorException;

    boolean blindPool(String actionID, String userSchemaName, String tableName, String columnName, String poolName) throws NormalErrorException;

    boolean registerRuler(String actionID, String userSchemaName,
                          String tableName, String columnName, String builderRuler) throws NormalErrorException;

    ColumnBuilderInfo readRuler(String actionID, String userSchemaName, String tableName, String columnName,
                                List<ColumnBuilderInfo> builderInfos);

    ColumnBuilderInfo readRulerFromDB(String actionID, String databaseName, String tableName, String column);

    List<ColumnBuilderInfo> getRulerList(String actionID) throws NormalErrorException;

    void clearRulers(String actionID) throws NormalErrorException;

    Object randomByRuler(String buildRuler, List<String> params) throws NormalErrorException;

    List<String> createPool(String actionID, String poolName,
                            String buildRuler, List<String> params, int count) throws NormalErrorException;

    Object randomByRuler(String actionID,
                         SQLColumnDefinition sqlColumnDefinition,
                         MySqlInsertStatement mySqlInsertStatement,
                         List<ColumnBuilderInfo> rulerList) throws NormalErrorException;

    Object randomByType(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException;

    Object randomByColumn(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException;

    List<String> getDataByRegex(BuildRuler buildRuler) throws NormalErrorException;

    void registerRegexRuler(RegisterRulerVo registerRulerVo) throws NormalErrorException;

    List<String> testRuler(BuildRuler buildRuler) throws NormalErrorException;

    List<SelectOption> getRuler(String actionID);
}
