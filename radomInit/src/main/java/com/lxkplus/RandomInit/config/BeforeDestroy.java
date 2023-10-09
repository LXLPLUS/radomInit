package com.lxkplus.RandomInit.config;

import com.lxkplus.RandomInit.service.UserDatabaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class BeforeDestroy implements DisposableBean {

    @Resource
    UserDatabaseService userDatabaseService;

    @Value("${randomInit.destroy-all-exist}")
    boolean clearAllExist;

    @Override
    public void destroy() {
        if (clearAllExist) {
            List<String> stringList = userDatabaseService.forceDropAllTempSchema();
            log.info("清空临时数据库" + stringList.toString());
        }
    }
}
