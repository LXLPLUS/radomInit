package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.commons.CacheBuffer;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.UserMessage;
import com.lxkplus.RandomInit.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ActionIdServiceImpl implements ActionIdService {

    @Resource
    EnvService envService;

    @Resource
    SchemaService schemaService;

    @Resource
    RandomService randomService;

    @Resource
    TableService tableService;

    @Resource
    DataService dataService;

    @Resource
    MysqlCheckService mysqlCheckService;

    @Resource
    CacheBuffer cacheBuffer;

    static final String DEFAULT_DATABASE_NAME = "default";

    @Override
    public void createUserID(String actionID) throws NormalErrorException {
        // 生成默认数据库
        schemaService.createSchema(actionID, DEFAULT_DATABASE_NAME);
        // 指定活动数据库
        UserMessage env = envService.getEnv(actionID);
        env.setActionID(actionID);
        env.setActiveDatabase(DEFAULT_DATABASE_NAME);
        // 设置为第一步
        env.setStep(0);
    }

    @Override
    public void testCreate() throws NormalErrorException {

        String testActionID = "test01";

        dropActionID(testActionID);
        createUserID(testActionID);
        String sql = """
            CREATE TABLE `t_search_status` (
              `id` bigint NOT NULL AUTO_INCREMENT,
              `project_id` varchar(256) NOT NULL DEFAULT '' COMMENT 'id',
              `cut_id` int NOT NULL DEFAULT '-1' COMMENT 'id, 0',
              `status` varchar(32) NOT NULL DEFAULT 'created',
              `info` json DEFAULT NULL,
              `service_name` varchar(256) NOT NULL DEFAULT '',
              `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
              `enable` int NOT NULL DEFAULT '1',
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3;
            """;

        String sql2 = """
            CREATE TABLE `t_search_status2` (
              `id` bigint NOT NULL AUTO_INCREMENT,
              `project_id` varchar(256) NOT NULL DEFAULT '' COMMENT 'id',
              `cut_id` int NOT NULL DEFAULT '-1' COMMENT 'id, 0',
              `status` varchar(32) NOT NULL DEFAULT 'created',
              `info` json DEFAULT NULL,
              `service_name` varchar(256) NOT NULL DEFAULT '',
              `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00',
              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
              `enable` int NOT NULL DEFAULT '1',
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3;
            """;
        tableService.createTable(testActionID, "default", sql);
        tableService.createTable(testActionID, "default", sql2);

        randomService.registerRuler(testActionID, "default",
                "t_search_status", "status", "enum", List.of("12", "13", "启动", "正常"));
        randomService.registerRuler(testActionID, "default", "t_search_status", "project_id", "regex", "bot_id[a-z]{12}");

        dataService.fillRandomData(testActionID, "default", "t_search_status", 10);
    }

    @Override
    public void setActiveDatabase(String actionID, String userSchemaName) throws NormalErrorException {
        mysqlCheckService.checkDatabaseExist(actionID, userSchemaName, true);
        UserMessage env = envService.getEnv(actionID);
        env.setActiveDatabase(userSchemaName);
    }

    @Override
    public void addStep(String actionID) {
        UserMessage env = envService.getEnv(actionID);
        env.setStep(env.getStep() + 1);
    }

    @Override
    public String getActiveDatabase(String actionID, String userSchemaName) {
        return envService.getEnv(actionID).getActiveDatabase();
    }

    @Override
    public void dropActionID(String actionID) throws NormalErrorException {
        // 清空环境变量
        envService.deleteEnv(actionID);
        // 清空注册的规则
        randomService.clearRulers(actionID);
        // 清掉数据库
        schemaService.dropSchemaByID(actionID);
        // 清空缓存
        cacheBuffer.clearPool(actionID);
    }
}
