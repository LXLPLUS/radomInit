package com.lxkplus.RandomInit.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface EnvService {
    List<String> getEnv(String actionID, String key);

    void setEnv(String actionID, String key, List<String> value);

    void setEnv(String actionID, String key, String value);

    void deleteEnv(String actionID);

    ConcurrentHashMap<String, List<String>> getAllEnv(String actionID);

}
