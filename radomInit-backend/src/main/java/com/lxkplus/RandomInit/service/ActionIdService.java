package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface ActionIdService {
    void createUserID(String actionID) throws NormalErrorException;

    /**
     * 生成一个actionID为1的库，并写入数据
     */
    void testCreate() throws NormalErrorException;

    void setActiveDatabase(String actionID, String userSchemaName) throws NormalErrorException;

    void addStep(String actionID);

    String getActiveDatabase(String actionID, String userSchemaName);

    void dropActionID(String actionID) throws NormalErrorException;
}
