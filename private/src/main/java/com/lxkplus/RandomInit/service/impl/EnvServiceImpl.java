package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.service.EnvService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EnvServiceImpl implements EnvService {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, List<String>>> globalEnvMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, List<String>> getDefaultMap(String actionID) {
        ConcurrentHashMap<String, List<String>> temp = new ConcurrentHashMap<>();
        temp.put("actionID", List.of(actionID));
        return temp;
    }

    @Override
    public List<String> getEnv(String actionID, String key) {
        ConcurrentHashMap<String, List<String>> actionEnvMap;
        actionEnvMap = globalEnvMap.computeIfAbsent(actionID, k -> getDefaultMap(actionID));
        return actionEnvMap.getOrDefault(key, Collections.emptyList());
    }

    @Override
    public void setEnv(String actionID, String key, List<String> value) {
        globalEnvMap.computeIfAbsent(actionID, k -> getDefaultMap(actionID)).put(key, value);
    }

    @Override
    public void setEnv(String actionID, String key, String value) {
        globalEnvMap.computeIfAbsent(actionID, k -> getDefaultMap(actionID)).put(key, new ArrayList<>(List.of(value)));
    }

    @Override
    public void deleteEnv(String actionID) {
        globalEnvMap.remove(actionID);
    }

    @Override
    public ConcurrentHashMap<String, List<String>> getAllEnv(String actionID) {
        return globalEnvMap.getOrDefault(actionID, new ConcurrentHashMap<>());
    }
}