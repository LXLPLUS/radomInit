package com.lxkplus.RandomInit.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface TableActionMapper {

    @Insert("${sql}")
    void createTable(String sql);

    @Insert("${sql}")
    void fillRandomData(String sql);

    @Select("${sql}")
    List<LinkedHashMap<String, String>> SelectData(String sql);

    @Delete("delete from ${databaseName}.${tableName}")
    void clearData(String databaseName, String tableName);

    @Select("select count(*) from ${databaseName}.${tableName}")
    int countRows(String databaseName, String tableName);

    @Select("show create table ${databaseName}.${tableName}")
    Map<String, String> getCreateDDL(String databaseName, String tableName);

    @Select("select * from ${databaseName}.${tableName}")
    List<LinkedHashMap<String, String>> getData(String databaseName, String tableName);


}