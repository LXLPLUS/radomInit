package com.lxkplus.RandomInit.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DatabaseMapper {
    @Select("show databases like 'randomInit_temp_%'")
    List<String> getExistDatabase();

    @Insert("create database ${name}")
    void createDatabase(String name);

    @Delete("drop database ${name}")
    void dropByName(String name);
}
