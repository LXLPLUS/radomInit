package com.lxkplus.RandomInit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface JsonService {
    String getTableMessage(String actionID, String databaseName, String tableName) throws NormalErrorException, JsonProcessingException;
}
