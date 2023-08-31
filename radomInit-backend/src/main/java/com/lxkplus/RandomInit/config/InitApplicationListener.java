package com.lxkplus.RandomInit.config;

import com.lxkplus.RandomInit.mapper.TableActionMapper;
import com.lxkplus.RandomInit.service.SchemaService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    TableActionMapper tableActionMapper;

    @Resource
    SchemaService schemaService;


    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {

        log.info("============    初始化环境中…… =============");

        tableActionMapper.createTable("""
                CREATE TABLE IF NOT EXISTS `column_info` (
                  `id` int NOT NULL AUTO_INCREMENT,
                  `action_id` varchar(100) DEFAULT NULL,
                  `user_schema_name` varchar(100) DEFAULT NULL,
                  `table_name` varchar(100) DEFAULT NULL,
                  `column_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                  `builder_ruler` varchar(100) DEFAULT NULL,
                  `ruler_type` varchar(100) DEFAULT NULL,
                  `pool_name` varchar(200) DEFAULT NULL,
                  `params` text,
                  `insert_time` datetime DEFAULT CURRENT_TIMESTAMP,
                  PRIMARY KEY (`id`),
                  KEY `column_info_action_id_IDX` (`action_id`,`user_schema_name`,`table_name`,`column_name`) USING BTREE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                """);

        tableActionMapper.createTable("""
                CREATE TABLE IF NOT EXISTS `action_log` (
                  `id` int NOT NULL AUTO_INCREMENT,
                  `action_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                  `step` varchar(100) DEFAULT NULL,
                  `log_level` varchar(100) DEFAULT NULL,
                  `message` text,
                  `insert_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  PRIMARY KEY (`id`),
                  KEY `action_log_actionID_IDX` (`action_id`,`step`) USING BTREE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                """);

        tableActionMapper.createTable("""
                CREATE TABLE `t_pool_register` (
                  `id` int NOT NULL AUTO_INCREMENT,
                  `action_id` varchar(100) DEFAULT NULL,
                  `pool_name` varchar(100) DEFAULT NULL,
                  `ruler_type` varchar(100) DEFAULT NULL,
                  `params` text,
                  `insert_time` datetime DEFAULT NULL,
                  PRIMARY KEY (`id`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                """);

        schemaService.dropAllSchema();

        log.info("===============   项目初始化完成！ =============");
    }
}
