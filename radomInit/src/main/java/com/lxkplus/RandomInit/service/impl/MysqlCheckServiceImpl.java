package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.TableExistMapper;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MysqlCheckServiceImpl implements MysqlCheckService {

    @Resource
    TableExistMapper tableExistMapper;

    @Override
    public void checkTableNameSafe(String actionID, String userSchemaName, String tableName) throws NormalErrorException {
        checkActionIDSafe(actionID);
        checkDataBaseNameSafe(userSchemaName);
        checkTableNameSafe(tableName);
    }

    @Override
    public void checkActionIDSafe(String actionID) throws NormalErrorException {
        ThrowUtils.throwIfNullOrBlack(actionID, "actionID不能为空");
        ThrowUtils.throwIf(!actionID.matches("[A-Za-z0-9]+"), ErrorEnum.PARAM_NOT_SUPPORT,"actionID只支持英文字母和数字");
        ThrowUtils.throwIf(actionID.length() > 10, ErrorEnum.PARAM_NOT_SUPPORT, "id过长超过最大长度10！");
    }

    @Override
    public void checkDataBaseNameSafe(String userSchemaName) throws NormalErrorException {
        ThrowUtils.throwIfNullOrBlack(userSchemaName, "数据库名不能为空");
        ThrowUtils.throwIf(!userSchemaName.matches("\\w+"), ErrorEnum.PARAM_NOT_SUPPORT,"数据库名只支持英文字母和数字、下划线");
        ThrowUtils.throwIf(userSchemaName.length() > 30, ErrorEnum.PARAM_NOT_SUPPORT, "数据库名过长！");
    }

    @Override
    public void checkTableNameSafe(String tableName) throws NormalErrorException {
        ThrowUtils.throwIfNullOrBlack(tableName, "表名不能为空");
        ThrowUtils.throwIf(!tableName.matches("[A-Za-z\\d_`\\s]+"), ErrorEnum.PARAM_NOT_SUPPORT,"表名只支持英文字母和数字、下划线");
        ThrowUtils.throwIf(tableName.length() > 30, ErrorEnum.PARAM_NOT_SUPPORT, "表名过长！");
    }

    @Override
    public boolean checkDatabaseExist(String actionID, String userSchemaName, boolean strict) throws NormalErrorException {
        checkActionIDSafe(actionID);
        checkDataBaseNameSafe(userSchemaName);
        boolean empty = (tableExistMapper.getUserDatabaseCount(actionID, userSchemaName) == 0);
        if (strict && empty) {
            throw new NormalErrorException(ErrorEnum.NOT_EXIST, "查询的数据库" + userSchemaName + "不存在！");
        }
        return !empty;
    }

    @Override
    public boolean checkTableExist(String actionID, String userSchemaName, String tableName, boolean strict) throws NormalErrorException {
        checkActionIDSafe(actionID);
        checkTableNameSafe(tableName);
        checkDataBaseNameSafe(userSchemaName);

        boolean empty = (tableExistMapper.getTableCount(actionID, userSchemaName, tableName) == 0);
        if (strict && empty) {
            throw new NormalErrorException(ErrorEnum.NOT_EXIST, "查询的数据表不存在！");
        }
        return !empty;
    }

    @Override
    public boolean checkColumnExist(String actionID, String userSchemaName, String tableName, String column, boolean strict) throws NormalErrorException {
        checkActionIDSafe(actionID);
        checkDataBaseNameSafe(userSchemaName);
        checkTableNameSafe(tableName);

        boolean empty = (tableExistMapper.getColumnCount(actionID, userSchemaName, tableName, column) == 0);
        if (strict && empty) {
            throw new NormalErrorException(ErrorEnum.NOT_EXIST, "查询的数据列不存在！");
        }
        return !empty;
    }





}
