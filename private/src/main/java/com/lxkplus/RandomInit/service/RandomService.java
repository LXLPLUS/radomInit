package com.lxkplus.RandomInit.service;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.lxkplus.RandomInit.exception.NormalErrorException;

import java.util.List;

public interface RandomService {
    Object randomByType(String type, List<SQLExpr> arguments) throws NormalErrorException;
    Object randomByColumn(SQLColumnDefinition sqlColumnDefinition);
}
