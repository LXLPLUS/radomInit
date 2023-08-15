package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

import java.util.List;

public interface TableService {
    /**
     * 校验是否有sql注入的风险，简单来说就是只支持英文字符下滑线相关
     * @param actionID 操作id
     * @param databaseName 数据库名
     * @param tableName 表名
     * @throws NormalErrorException 如果不符合条件会抛出对应异常
     */
    void checkTable(String actionID, String databaseName, String tableName) throws NormalErrorException;

    boolean checkTableExist(String actionID, String databaseName, String tableName) throws NormalErrorException;

    /**
     * 检查某个人的某个数据库下有哪些表
     * @param actionID 用户id，全局唯一
     * @param databaseName 数据库名，单用户唯一
     * @param tableName 表格名，数据库内唯一
     * @return 表格列表
     */
    List<String> getTableList(String actionID, String databaseName, String tableName) throws NormalErrorException;

    List<List<String>> getTableList(String actionID, String databaseName);

    List<List<String>> getTableList(String actionID) throws NormalErrorException;

    void createTable(String actionID, String databaseName, String sql) throws NormalErrorException;

    int clearData(String actionID, String databaseName, String tableName) throws NormalErrorException;

    String getTableDDL(String actionID, String databaseName, String tableName) throws NormalErrorException;
}
