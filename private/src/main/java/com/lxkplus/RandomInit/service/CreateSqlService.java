package com.lxkplus.RandomInit.service;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.lxkplus.RandomInit.exception.NormalErrorException;

import java.util.List;

public interface CreateSqlService {
    MySqlCreateTableStatement readCreateSql(String sql) throws NormalErrorException;
    String statementToCreateDDL(MySqlCreateTableStatement statement) throws NormalErrorException;

    List<String> createToInsert(String sql, int count) throws NormalErrorException;
    String selectFill(String selectSql, String createSql) throws NormalErrorException;
}
