package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxkplus.RandomInit.commons.CacheBuffer;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.MysqlEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.ColumnInfoMapper;
import com.lxkplus.RandomInit.model.ColumnInfo;
import com.lxkplus.RandomInit.service.DatabaseService;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import com.lxkplus.RandomInit.service.RandomService;
import com.lxkplus.RandomInit.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RandomServiceImpl implements RandomService {

    @Resource
    JsonUtils jsonUtils;

    @Resource
    ObjectMapper objectMapper;


    @Resource
    DatabaseService databaseService;

    @Resource
    MysqlCheckService mysqlCheckService;

    @Resource
    ColumnInfoMapper columnInfoMapper;

    @Resource
    CacheBuffer cacheBuffer;


    @Override
    public boolean registerRuler(String actionID, String userDatabaseName,
                                 String tableName, String column, String builderRuler,
                                 List<String> params) throws NormalErrorException {
        boolean exist = mysqlCheckService.checkColumnExist(actionID, userDatabaseName, tableName, column, false);
        if (!exist) {
            return false;
        }

        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setActionID(actionID);
        columnInfo.setUserDatabaseName(userDatabaseName);
        columnInfo.setTableName(tableName);
        columnInfo.setColumnName(column);
        columnInfo.setBuilderRuler(builderRuler);
        columnInfo.setParams(StringUtils.join(params, " "));
        columnInfo.setInsertTime(new Date());

        columnInfoMapper.insert(columnInfo);
        return true;
    }

    @Override
    public boolean registerRuler(String actionID, String userDatabaseName,
                                 String tableName, String column, String builderRuler, String params) throws NormalErrorException {
        return registerRuler(actionID, userDatabaseName, tableName, column, builderRuler, List.of(params));
    }

    @Override
    public boolean registerRuler(String actionID, String userDatabaseName,
                                 String tableName, String column, String builderRuler) throws NormalErrorException {
        return registerRuler(actionID, userDatabaseName, tableName, column, builderRuler, Collections.emptyList());
    }

    @Override
    public ColumnInfo readRuler(String actionID, String userDatabaseName, String tableName, String column) {
        List<ColumnInfo> columnInfos = columnInfoMapper.selectByMap(Map.of(
                "action_id", actionID,
                "user_database_name", userDatabaseName,
                "table_name", tableName,
                "column_name", column));
        if (columnInfos == null || columnInfos.isEmpty()) {
            return null;
        }
        columnInfos.sort(Comparator.comparing(x -> -x.getInsertTime().getTime()));
        return columnInfos.get(0);
    }

    @Override
    public List<ColumnInfo> getRulerList(String actionID) {
        return columnInfoMapper.selectByMap(Map.of("action_id", actionID));
    }

    @Override
    public void clearRulers(String actionID) {
        columnInfoMapper.deleteByMap(Map.of("action_id", actionID));
    }

    @Override
    public Object randomByRuler(String buildRuler, List<String> params) throws NormalErrorException {
        if (StringUtils.isBlank(buildRuler)) {
            return null;
        }
        switch (buildRuler){
            case "正则":
            case "正则表达式":
            case "regex":
                ThrowUtils.throwIf(params.isEmpty(), ErrorEnum.NotEnoughParams, "缺乏正则表达式参数");
                return jsonUtils.faker.regexify(params.get(0));
            case "long":
            case "int":
            case "integer":
            case "数字":
            case "range":
            case "范围":
                if (params.isEmpty()) {
                    return jsonUtils.faker.number().numberBetween(0, Integer.MAX_VALUE / 10);
                }
                else if (params.size() == 1) {
                    return jsonUtils.faker.number().numberBetween(Long.parseLong(params.get(0)), Integer.MAX_VALUE / 2);
                }
                return jsonUtils.faker.number().numberBetween(Long.parseLong(params.get(0)), Long.parseLong(params.get(1)));
            case "正数":
                return jsonUtils.faker.number().numberBetween(1, Integer.MAX_VALUE / 10);
            case "手机号":
                return jsonUtils.faker.regexify("1[0-9]{10}");
            case "ip":
            case "ipv4":
                return jsonUtils.faker.internet().ipV4Address();
            case "uuid":
                return jsonUtils.faker.internet().uuid();
            case "url":
                return jsonUtils.faker.internet().url();
            case "名字":
                return jsonUtils.faker.name().name();
            case "default":
            case "默认值":
            case "枚举":
            case "enum":
                ThrowUtils.throwIf(params.isEmpty(), ErrorEnum.NotEnoughParams, "缺乏默认值");
                int index = jsonUtils.faker.number().numberBetween(0, params.size());
                return params.get(index);
            case "金钱":
            case "钱":
            case "money":
                return jsonUtils.faker.number().randomDouble(2, 0, 1000000);
            case "邮箱":
            case "mail":
            case "email":
                return jsonUtils.faker.internet().emailAddress(jsonUtils.faker.funnyName().name());
            default:
                return null;
        }
    }

    @Override
    public Object randomByRuler(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException {
        String columnName = StringUtils.strip(sqlColumnDefinition.getColumnName(), "`");
        String tableName = StringUtils.strip(mySqlInsertStatement.getTableSource().getTableName(), "`");
        String schema = StringUtils.strip(mySqlInsertStatement.getTableSource().getSchema(), "`");

        String userDatabaseName = databaseService.getFromRealDatabaseName(schema).getRight();

        ColumnInfo columnInfo = readRuler(actionID, userDatabaseName, tableName, columnName);
        if (columnInfo == null) {
            return null;
        }
        return randomByRuler(columnInfo.getBuilderRuler(), List.of(columnInfo.getParams().split("\\s+")));
    }

    @Override
    public Object randomByType(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException {
        MysqlEnum mysqlEnum = MysqlEnum.valueOf(sqlColumnDefinition.getDataType().getName().toUpperCase());
        List<SQLExpr> arguments = sqlColumnDefinition.getDataType().getArguments();
        switch (mysqlEnum) {
            case INT:
                return jsonUtils.faker.number().numberBetween(0, Integer.MAX_VALUE / 10);
            case BIGINT:
            case LONG:
                return jsonUtils.faker.number().numberBetween(0, Long.MAX_VALUE / 10);
            case DOUBLE:
                return jsonUtils.faker.number().randomDouble(3, 0, Long.MAX_VALUE / 100);
            case FLOAT:
                return jsonUtils.faker.number().randomDouble(3, 0, Integer.MAX_VALUE / 2);
            case VARCHAR:
                int sqlExpr = ((SQLIntegerExpr) arguments.get(0)).getNumber().intValue() - 10;
                sqlExpr = Math.max(sqlExpr, 2);
                return RandomStringUtils.randomPrint(0, sqlExpr).replaceAll("['\"\\\\{}]", "");
            case DATETIME:
            case DATE:
            case TIMESTAMP:
                Date startTime = DateUtils.addDays(new Date(), -1);
                String message = String.format("%s_%s_%s",
                        mySqlInsertStatement.getTableSource().getTableName(),
                        mySqlInsertStatement.getTableSource().getSchema(),
                        sqlColumnDefinition.getName());
                return objectMapper.convertValue(cacheBuffer.getTime(actionID, message, startTime, 100, 500), String.class);
            case TEXT:
                return RandomStringUtils.randomPrint(0, 500);
            case JSON:
                return null;
            case DECIMAL:
                int sqlExpr1 = ((SQLIntegerExpr) arguments.get(0)).getNumber().intValue();
                int sqlExpr2 = ((SQLIntegerExpr) arguments.get(1)).getNumber().intValue();
                return jsonUtils.faker.number().randomDouble(sqlExpr2,
                        0,
                        (long) Math.pow(10, sqlExpr1 - sqlExpr2) - 1);
            case TINYINT:
            case BOOL:
                return jsonUtils.faker.number().numberBetween(0, 2);
            default:
                throw new NormalErrorException(ErrorEnum.paramNotSupport, String.format("%s这个参数不支持",
                        sqlColumnDefinition.getDataType().getName().toUpperCase()));
        }
    }

    @Override
    public Object randomByColumn(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) {
        if (sqlColumnDefinition.isAutoIncrement()) {
            return 0;
        }
        if (StringUtils.containsAny(sqlColumnDefinition.getColumnName(),
                "insert_time",
                "update_time",
                "create_time")) {
            String tableMessage = String.format("%s_%s_%s",
                    mySqlInsertStatement.getTableSource().getTableName(),
                    mySqlInsertStatement.getTableSource().getSchema(),
                    sqlColumnDefinition.getName());
            // 从24小时之前填入数据，1分钟1次
            Date startTime = DateUtils.addDays(new Date(), -1);
            return cacheBuffer.getTime(actionID, tableMessage, startTime);
        }
        return null;
    }

}
