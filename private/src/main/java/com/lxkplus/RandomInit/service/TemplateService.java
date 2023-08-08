package com.lxkplus.RandomInit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.MysqlCreate;
import freemarker.template.TemplateException;
import net.sf.jsqlparser.JSQLParserException;

import java.io.IOException;

public interface TemplateService {
    String mysqlTableBuilder(MysqlCreate mysqlCreate) throws NormalErrorException, IOException, TemplateException;
    MysqlCreate sqlParser(String sql) throws NormalErrorException, JSQLParserException, JsonProcessingException;
}
