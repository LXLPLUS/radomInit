package com.lxkplus.RandomInit.mapper;

import com.lxkplus.RandomInit.model.entity.Schema;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DatabaseMapper {
    @Select("select SCHEMA_NAME as 'schema_name'  from information_schema.SCHEMATA s where SCHEMA_NAME like 'randominit_temp_%'")
    List<Schema> getExistDatabase();

    @Insert("create database if not exists ${realSchemaName}")
    void createDatabase(String realSchemaName);

    @Delete("drop database ${realSchemaName}")
    void dropByName(String realSchemaName);
}
