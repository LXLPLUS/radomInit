package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.mapper.ActionLogMapper;
import com.lxkplus.RandomInit.model.DBModel.LogMessage;
import com.lxkplus.RandomInit.service.LogService;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Resource
    MysqlCheckService mysqlCheckService;

    @Resource
    ActionLogMapper actionLogMapper;

    @Override
    public List<LogMessage> getLog(String actionID, Integer step, String level) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        return actionLogMapper.selectMessage(actionID, step, level);
    }
}
