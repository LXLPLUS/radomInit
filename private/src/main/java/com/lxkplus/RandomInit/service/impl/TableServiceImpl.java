package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.TableMapper;
import com.lxkplus.RandomInit.service.CreateSqlService;
import com.lxkplus.RandomInit.service.DatabaseService;
import com.lxkplus.RandomInit.service.TableService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableServiceImpl implements TableService {

    @Resource
    DatabaseService databaseService;

    @Resource
    CreateSqlService sqlService;

    @Resource
    TableMapper tableMapper;

    @Override
    public void checkTable(String actionID, String databaseName, String tableName) throws NormalErrorException {
        databaseService.checkIDAndName(actionID, databaseName);
        ThrowUtils.throwIfNullOrBlack(tableName, "表名不能为空");
        ThrowUtils.throwIf(!tableName.matches("[A-Za-z0-9_`]+"), ErrorEnum.paramNotSupport,"表名只支持英文字母和数字、下划线");
        ThrowUtils.throwIf(tableName.length() > 30, ErrorEnum.paramNotSupport, "表名过长！");
    }

    @Override
    public boolean checkTableExist(String actionID, String databaseName, String tableName) throws NormalErrorException {
        checkTable(actionID, databaseName, tableName);
        String realDatabaseName = databaseService.getRealDatabaseName(actionID, databaseName);
        return tableMapper.checkTableExist(realDatabaseName, tableName) == 1;
    }

    @Override
    public List<String> getTableList(String actionID, String databaseName, String tableName) throws NormalErrorException {
        checkTable(actionID, databaseName, tableName);
        String realDatabaseName = databaseService.getRealDatabaseName(actionID, databaseName);
        return tableMapper.getTableList(realDatabaseName);
    }

    @Override
    public List<String> getTableList(String actionID, String databaseName) {
        String realDatabaseName = databaseService.getRealDatabaseName(actionID, databaseName);
        return tableMapper.getTableList(realDatabaseName);
    }

    @Override
    public void createTable(String actionID, String databaseName, String sql) throws NormalErrorException {
        databaseService.checkIDAndName(actionID, databaseName);
        String realDatabaseName = databaseService.getRealDatabaseName(actionID, databaseName);
        ThrowUtils.throwIf(!databaseService.checkDatabaseExist(actionID, databaseName),
                ErrorEnum.Empty,
                realDatabaseName + "数据库不存在");


        MySqlCreateTableStatement statement = sqlService.readCreateSql(sql);
        String tableName = StringUtils.strip(statement.getTableName(), "`");

        // 如果数据库存在那么跳过
        ThrowUtils.throwIf(checkTableExist(actionID, databaseName, tableName),
                ErrorEnum.Exist,
                String.format("action:%s 数据库:%s对应表%s存在", actionID, databaseName, tableName));
        statement.setIfNotExiists(false);
        statement.setSchema(realDatabaseName);

        tableMapper.createTable(statement.toString());
    }


}
