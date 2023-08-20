package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.mapper.ViewMapper;
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

    @Override
    public void createView(String actionID, String viewName, String selectSql, String activeDatabaseName)
            throws NormalErrorException {

        mysqlCheckService.checkActionIDSafe(actionID);
        mysqlCheckService.checkTableNameSafe(viewName);
        String sql = selectService.SqlConvert(selectSql, actionID, activeDatabaseName);
        viewMapper.createView(actionID, viewName, sql);
    }
}
