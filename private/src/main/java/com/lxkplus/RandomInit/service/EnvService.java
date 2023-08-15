package com.lxkplus.RandomInit.service;

public interface EnvService {
    String getENV(String actionID, String key);

    void setEnv(String actionID, String key, String value);

    String getAllEnv(String actionID);
}
