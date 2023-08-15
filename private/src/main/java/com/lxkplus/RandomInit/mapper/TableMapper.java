package com.lxkplus.RandomInit.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface TableMapper {

    @Select("""
            select table_name from information_schema.TABLES where TABLE_SCHEMA = #{databaseName}
            and TABLE_SCHEMA like 'randomInit_temp_%'
            """)
    List<String> getTableList(String databaseName);

    @Select("select TABLE_SCHEMA, table_name from information_schema.TABLES where TABLE_SCHEMA like 'randomInit_temp_%'")
    List<String> getAllTable();

    @Select("""
            select count(*) from information_schema.TABLES where TABLE_SCHEMA = #{databaseName}
            and table_name = #{tableName}
            and TABLE_SCHEMA like 'randomInit_temp_%'
            """)
    int checkTableExist(String databaseName, String tableName);

    @Insert("${sql}")
    void createTable(String sql);

    @Insert("${sql}")
    void FillRandomData(String sql);

    @Delete("delete from ${databaseName}.${tableName}")
    void clearData(String databaseName, String tableName);

    @Select("select count(*) from ${databaseName}.${tableName}")
    int countRows(String databaseName, String tableName);

    @Select("show create table ${databaseName}.${tableName}")
    Map<String, String> getCreateTable(String databaseName, String tableName);

    @Select("""
            select * from information_schema.`COLUMNS`
            where TABLE_SCHEMA like 'randomInit_temp_%'
            and TABLE_SCHEMA = #{databaseName}
            and TABLE_NAME = #{tableName}
            """)
    List<Map<String, String>> getColumn(String databaseName, String tableName);

}