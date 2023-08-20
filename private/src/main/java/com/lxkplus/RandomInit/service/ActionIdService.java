package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;

public interface ActionIdService {
    void createUserID(String actionID) throws NormalErrorException;

    void setActiveDatabase(String actionID, String userDatabaseName) throws NormalErrorException;

    void addStep(String actionID);

    String getActiveDatabase(String actionID, String userDatabaseName);

    void dropActionID(String actionID) throws NormalErrorException;
}
