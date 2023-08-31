package com.lxkplus.RandomInit.config;

import com.lxkplus.RandomInit.service.SchemaService;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BeforeDestroy implements DisposableBean {

    @Resource
    SchemaService schemaService;

    @Value("${randomInit.destroy-all-exist}")
    Boolean clearAllExist;

    @Override
    public void destroy() {

        // 清空日志挂载日志
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            ctx.getConfiguration().getRootLogger().removeAppender("databaseAppender");

        if (clearAllExist) {
            schemaService.dropAllSchema();
        }
    }
}
