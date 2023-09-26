package com.lxkplus.RandomInit.config;

import com.lxkplus.RandomInit.service.SchemaService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BeforeDestroy implements DisposableBean {

    @Resource
    SchemaService schemaService;

    @Value("${randomInit.destroy-all-exist}")
    boolean clearAllExist;

    @Override
    public void destroy() {

        if (clearAllExist) {
            schemaService.dropAllSchema();
        }
    }
}
