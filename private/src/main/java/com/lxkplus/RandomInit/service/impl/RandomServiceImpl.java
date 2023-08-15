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
import com.lxkplus.RandomInit.mapper.BuilderMapper;
import com.lxkplus.RandomInit.service.RandomService;
import com.lxkplus.RandomInit.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RandomServiceImpl implements RandomService {

    @Resource
    JsonUtils jsonUtils;

    @Resource
    ObjectMapper objectMapper;

    @Resource
    BuilderMapper builderMapper;

    @Resource
    CacheBuffer cacheBuffer;

    @Override
    public Object randomByType(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException {
        MysqlEnum mysqlEnum = MysqlEnum.valueOf(sqlColumnDefinition.getDataType().getName().toUpperCase());
        List<SQLExpr> arguments = sqlColumnDefinition.getDataType().getArguments();
        switch (mysqlEnum) {
            case INT:
                return jsonUtils.faker.number().numberBetween(Integer.MIN_VALUE / 10, Integer.MAX_VALUE / 10);
            case BIGINT:
            case LONG:
                return jsonUtils.faker.number().numberBetween(Integer.MIN_VALUE / 10, Long.MAX_VALUE / 10);
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
                String actionIDWithTable = String.format("%s_%s_%s_%s",
                        actionID,
                        mySqlInsertStatement.getTableSource().getTableName(),
                        mySqlInsertStatement.getTableSource().getSchema(),
                        sqlColumnDefinition.getName());
                return objectMapper.convertValue(cacheBuffer.getTime(actionIDWithTable, startTime, 100, 500), String.class);
            case TEXT:
                return RandomStringUtils.randomPrint(0, 500);
            case JSON:
                return null;
            case DECIMAL:
                int sqlExpr1 = ((SQLIntegerExpr) arguments.get(0)).getNumber().intValue();
                int sqlExpr2 = ((SQLIntegerExpr) arguments.get(1)).getNumber().intValue();
                return jsonUtils.faker.number().randomDouble(sqlExpr2,
                        (long) -Math.pow(10, sqlExpr1 - sqlExpr2) + 1,
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
            String actionIDWithTable = String.format("%s_%s_%s_%s",
                    actionID,
                    mySqlInsertStatement.getTableSource().getTableName(),
                    mySqlInsertStatement.getTableSource().getSchema(),
                    sqlColumnDefinition.getName());
            // 从24小时之前填入数据，1分钟1次
            Date startTime = DateUtils.addDays(new Date(), -1);
            return cacheBuffer.getTime(actionIDWithTable, startTime);
        }
        return null;
    }

}
