package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface ViewService {
    void createView(String actionID, String viewName, String selectSql, String activeSchemaName)
            throws NormalErrorException;

}
