package com.lxkplus.RandomInit.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TableMapper {
    @Select("select table_name from information_schema.TABLES where TABLE_SCHEMA = #{databaseName}")
    List<String> getTableList(String databaseName);

    @Select("select count(*) from information_schema.TABLES where TABLE_SCHEMA = #{databaseName} and table_name = #{tableName}")
    int checkTableExist(String databaseName, String tableName);

    @Insert("${sql}")
    void createTable(String sql);

}
