package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.model.UserMessage;
import com.lxkplus.RandomInit.service.EnvService;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class EnvServiceImpl implements EnvService {

    private final ConcurrentHashMap<String, UserMessage> globalEnvMap = new ConcurrentHashMap<>();

    public UserMessage getEnv(String actionID) {
        UserMessage userMessage = globalEnvMap.computeIfAbsent(actionID, k -> new UserMessage());
        if (userMessage.getActionID() == null) {
            userMessage.setActionID(actionID);
            userMessage.setLevel(-100);
        }
        return userMessage;
    }

    @Override
    public void deleteEnv(String actionID) {
        globalEnvMap.remove(actionID);
        MDC.clear();
    }
}