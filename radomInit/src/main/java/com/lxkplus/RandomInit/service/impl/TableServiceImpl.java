package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.TableActionMapper;
import com.lxkplus.RandomInit.service.SchemaService;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import com.lxkplus.RandomInit.service.StatementService;
import com.lxkplus.RandomInit.service.TableService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TableServiceImpl implements TableService {

    @Resource
    SchemaService schemaService;

    @Resource
    StatementService sqlService;

    @Resource
    TableActionMapper tableMapper;

    @Resource
    MysqlCheckService mysqlCheckService;

    @Override
    public void createTable(String actionID, String userSchemaName, String sql) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        mysqlCheckService.checkDataBaseNameSafe(userSchemaName);

        String realSchemaName = schemaService.getRealSchemaName(actionID, userSchemaName);
        ThrowUtils.throwIf(!mysqlCheckService.checkDatabaseExist(actionID, userSchemaName, false),
                ErrorEnum.Empty,
                realSchemaName + "数据库不存在");

        MySqlCreateTableStatement statement = sqlService.readCreateSql(sql);
        String tableName = StringUtils.strip(statement.getTableName(), "`");

        // 如果数据库存在那么跳过
        ThrowUtils.throwIf(mysqlCheckService.checkTableExist(actionID, userSchemaName, tableName, false),
                ErrorEnum.Exist,
                String.format("action:%s 数据库:%s对应表%s存在", actionID, userSchemaName, tableName));
        statement.setIfNotExiists(false);
        statement.setSchema(realSchemaName);

        tableMapper.createTable(statement.toString());
    }

    @Override
    public int clearData(String actionID, String userSchemaName, String tableName) throws NormalErrorException {
        // 检查参数是否合法和是否存在
        mysqlCheckService.checkTableNameSafe(actionID, userSchemaName, tableName);

        ThrowUtils.throwIf(!mysqlCheckService.checkTableExist(actionID, userSchemaName, tableName, false),
                ErrorEnum.Empty,
                String.format("操作的表格%s不存在！", tableName));

        String realSchemaName = schemaService.getRealSchemaName(actionID, userSchemaName);

        int count = tableMapper.countRows(realSchemaName, tableName);
        tableMapper.clearData(realSchemaName, tableName);
        return count;
    }

    @Override
    public String getTableDDL(String actionID, String databaseName, String tableName) throws NormalErrorException {
        // DDL里面没有数据库信息
        mysqlCheckService.checkTableNameSafe(actionID, databaseName, tableName);

        ThrowUtils.throwIf(!mysqlCheckService.checkTableExist(actionID, databaseName, tableName, false),
                ErrorEnum.Empty,
                String.format("操作的表格%s不存在！", tableName));

        String realSchemaName = schemaService.getRealSchemaName(actionID, databaseName);

        Map<String, String> params = tableMapper.getCreateDDL(realSchemaName, tableName);
        return params.getOrDefault("Create Table", null);
    }

}
