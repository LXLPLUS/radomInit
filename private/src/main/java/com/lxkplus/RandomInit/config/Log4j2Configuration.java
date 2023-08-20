package com.lxkplus.RandomInit.config;

import com.alibaba.druid.pool.DruidDataSource;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class Log4j2Configuration implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    DataSource dataSource;

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final ColumnConfig[] cc = {
                ColumnConfig.newBuilder().setConfiguration(ctx.getConfiguration())
                        .setName("action_id")
                        .setPattern("%X{actionID}")
                        .setUnicode(false)
                        .build(),
                ColumnConfig.newBuilder().setConfiguration(ctx.getConfiguration())
                        .setName("step")
                        .setPattern("%X{step}")
                        .setUnicode(false).build(),
                ColumnConfig.newBuilder().setConfiguration(ctx.getConfiguration())
                        .setName("log_level")
                        .setPattern("%level")
                        .build(),
                ColumnConfig.newBuilder().setConfiguration(ctx.getConfiguration())
                        .setName("message")
                        .setPattern("%m")
                        .build()
        };

        //配置appender
        final Appender appender = JdbcAppender
                .newBuilder()
                .setName("databaseAppender")
                .setIgnoreExceptions(false)
                .setConnectionSource(new ConnectionSource() {
                    @Override
                    public Connection getConnection() throws SQLException {
                        return dataSource.getConnection();
                    }

                    @Override
                    public State getState() {
                        return null;
                    }

                    @Override
                    public void initialize() {

                    }

                    @Override
                    public void start() {
                    }

                    @Override
                    public void stop() {
                    }

                    @Override
                    public boolean isStarted() {
                        if (dataSource instanceof DruidDataSource dataSource) {
                            return dataSource.isEnable();
                        }
                        return false;
                    }

                    @Override
                    public boolean isStopped() {
                        if (dataSource instanceof DruidDataSource dataSource) {
                           return dataSource.isClosed();
                        }
                        return false;
                    }
                })
                .setTableName("action_log")
                .setColumnConfigs(cc)
                .build();
        appender.start();

        ctx.getConfiguration().addAppender(appender);
        //指定哪些logger输出的日志保存在mysql中
        ctx.getConfiguration().getLoggerConfig("*").addAppender(appender, null, null);
        ctx.updateLoggers();
    }
}
