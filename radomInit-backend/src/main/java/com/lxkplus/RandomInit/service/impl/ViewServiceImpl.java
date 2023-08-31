package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.mapper.ViewMapper;
import com.lxkplus.RandomInit.model.UserMessage;
import com.lxkplus.RandomInit.service.EnvService;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import com.lxkplus.RandomInit.service.SelectService;
import com.lxkplus.RandomInit.service.ViewService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ViewServiceImpl implements ViewService {

    @Resource
    SelectService selectService;

    @Resource
    ViewMapper viewMapper;

    @Resource
    MysqlCheckService mysqlCheckService;

    @Resource
    EnvService envService;

    @Override
    public void createView(String actionID, String viewName, String selectSql, String activeSchemaName)
            throws NormalErrorException {

        mysqlCheckService.checkActionIDSafe(actionID);
        mysqlCheckService.checkTableNameSafe(viewName);
        String sql = selectService.SqlConvert(selectSql, actionID, activeSchemaName);
        viewMapper.createView(actionID, viewName, sql);
    }


    public String getRandomViewName(String actionID) {
        UserMessage env = envService.getEnv(actionID);
        Integer viewIndex = env.getViewIndex();
        env.setViewIndex(viewIndex + 1);
        return "view" + viewIndex;
    }
}
