package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.model.UserMessage;

public interface EnvService {
    UserMessage getEnv(String actionID);

    void deleteEnv(String actionID);

}
