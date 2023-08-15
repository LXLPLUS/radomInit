package com.lxkplus.RandomInit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.mapper.TableMapper;
import com.lxkplus.RandomInit.service.DatabaseService;
import com.lxkplus.RandomInit.service.JsonService;
import com.lxkplus.RandomInit.service.TableService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class JsonServiceImpl implements JsonService {

    @Resource
    TableService tableService;

    @Resource
    DatabaseService databaseService;

    @Resource
    ObjectMapper objectMapper;

    @Resource
    TableMapper tableMapper;

    @Override
    public String getTableMessage(String actionID, String databaseName, String tableName) throws NormalErrorException, JsonProcessingException {
        if (tableService.checkTableExist(actionID, databaseName, tableName)) {
            String realDatabaseName = databaseService.getRealDatabaseName(actionID, databaseName);
            return objectMapper.writeValueAsString(tableMapper.getColumn(realDatabaseName, tableName));
        }
        return "";
    }

}
