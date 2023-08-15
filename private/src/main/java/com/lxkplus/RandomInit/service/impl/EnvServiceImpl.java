package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.service.EnvService;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EnvServiceImpl implements EnvService {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> envMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> defaultMap = new ConcurrentHashMap<>();

    EnvServiceImpl() {
        // todo 实现这个方法
        defaultMap.put("map-underscore-to-camel-case", "true");
    }

    @Override
    public String getENV(String actionID, String key) {
        ConcurrentHashMap<String, String> actionEnvMap = envMap.computeIfAbsent(actionID, k -> new ConcurrentHashMap<>(defaultMap));
        return actionEnvMap.getOrDefault(key, null);
    }

    @Override
    public void setEnv(String actionID, String key, String value) {
        envMap.computeIfAbsent(actionID, k -> new ConcurrentHashMap<>(defaultMap)).put(key, value);
    }

    @Override
    public String getAllEnv(String actionID) {
        ConcurrentHashMap<String, String> actionEnvMap = envMap.getOrDefault(actionID, new ConcurrentHashMap<>(defaultMap));
        StringJoiner stringJoiner = new StringJoiner("\n");
        actionEnvMap.forEach((key, value) -> stringJoiner.add(String.format("%s = %s", key, value)));
        return stringJoiner.toString();
    }
}
