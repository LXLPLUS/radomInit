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
                    `id` INT NOT NULL AUTO_INCREMENT,
                    `action_id` VARCHAR(100) DEFAULT NULL,
                    `user_schema_name` VARCHAR(100) DEFAULT NULL,
                    `table_name` VARCHAR(100) DEFAULT NULL,
                    `column_name` VARCHAR(100) DEFAULT NULL,
                    `builder_ruler` VARCHAR(100) DEFAULT NULL,
                    `ruler_type` VARCHAR(100) DEFAULT NULL,
                    `pool_name` VARCHAR(200) DEFAULT NULL,
                    `params` TEXT,
                    `insert_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (`id`)
                ) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = ''
                """);

        tableActionMapper.createTable("""
                CREATE TABLE IF NOT EXISTS `action_log` (
                    `id` INT NOT NULL AUTO_INCREMENT,
                    `action_id` VARCHAR(100) NOT NULL,
                    `step` VARCHAR(100) DEFAULT NULL,
                    `log_level` VARCHAR(100) DEFAULT NULL,
                    `message` TEXT,
                    `insert_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (`id`)
                ) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = ''
                """);

        tableActionMapper.createTable("""
                CREATE TABLE IF NOT EXISTS`t_pool_register` (
                  `id` int NOT NULL AUTO_INCREMENT,
                  `action_id` varchar(100) DEFAULT NULL,
                  `pool_name` varchar(100) DEFAULT NULL,
                  `ruler_type` varchar(100) DEFAULT NULL,
                  `params` text,
                  `insert_time` datetime DEFAULT NULL,
                  PRIMARY KEY (`id`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                """);

        tableActionMapper.createTable("""
                CREATE TABLE IF NOT EXISTS `url_path` (
                    `id` INT(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
                    `uuid` VARCHAR(255) NOT NULL COMMENT 'uuid',
                    `action_id` VARCHAR(255) NOT NULL,
                    `create_time` DATETIME NOT NULL COMMENT '新建时间',
                    `is_delete` BOOLEAN NOT NULL DEFAULT '0' COMMENT '是否删除',
                    PRIMARY KEY (`id`),
                    KEY (`action_id`),
                    KEY (`uuid`)
                ) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
                """);

        schemaService.dropAllSchema();

        log.info("===============   项目初始化完成！ =============");
    }
}
