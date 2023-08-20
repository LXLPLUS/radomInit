package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ActionIdServiceImpl implements ActionIdService {

    @Resource
    EnvService envService;

    @Resource
    DatabaseService databaseService;

    @Resource
    RandomService randomService;

    @Resource
    MysqlCheckService mysqlCheckService;

    static final String defaultDatabaseName = "default";
    static final String activeDatabaseKey = "activeDatabase";
    static final String step = "step";

    @Override
    public void createUserID(String actionID) throws NormalErrorException {
        // 生成默认数据库
        databaseService.createDatabase(actionID, defaultDatabaseName);
        // 指定活动数据库
        envService.setEnv(actionID, activeDatabaseKey, defaultDatabaseName);
        // 设置为第一步
        envService.setEnv(step, "0");
        MDC.put(step, 0);
    }

    @Override
    public void setActiveDatabase(String actionID, String userDatabaseName) throws NormalErrorException {
        mysqlCheckService.checkDatabaseExist(actionID, userDatabaseName, true);
        envService.setEnv(actionID, activeDatabaseKey, userDatabaseName);
    }

    @Override
    public void addStep(String actionID) {
        List<String> env = envService.getEnv(actionID, step);
        int stepNum = Integer.parseInt(env.get(0)) + 1;
        envService.setEnv(step, Integer.toString(stepNum));
    }

    @Override
    public String getActiveDatabase(String actionID, String userDatabaseName) {
        return envService.getEnv(actionID, activeDatabaseKey).get(0);
    }

    @Override
    public void dropActionID(String actionID) throws NormalErrorException {
        // 清空环境变量
        envService.deleteEnv(actionID);
        // 清空注册的规则
        randomService.clearRulers(actionID);
        // 清掉数据库
        databaseService.dropDatabaseByID(actionID);
    }
}
