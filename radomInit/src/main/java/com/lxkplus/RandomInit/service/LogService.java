package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.DO.LogMessage;

import java.util.List;

public interface LogService {
    List<LogMessage> getLog(String actionID, Integer step, String level) throws NormalErrorException;
}
