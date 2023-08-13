package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.MysqlEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.service.RandomService;
import com.lxkplus.RandomInit.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
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

    @Override
    public Object randomByType(String type, List<SQLExpr> arguments) throws NormalErrorException {
        MysqlEnum mysqlEnum = MysqlEnum.valueOf(type.toUpperCase());
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
                return RandomStringUtils.randomPrint(0, sqlExpr).replaceAll("['\"\\\\]", "");
            case DATETIME:
            case DATE:
            case TIMESTAMP:
                Date startTime = DateUtils.addDays(new Date(), -200);
                Date endTime = DateUtils.addDays(new Date(), 200);
                return objectMapper.convertValue(jsonUtils.faker.date().between(startTime, endTime), String.class);
            case TEXT:
                return RandomStringUtils.randomPrint(0, 500);
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
                throw new NormalErrorException(ErrorEnum.paramNotSupport, String.format("%s这个参数不支持", type));
        }
    }

    @Override
    public Object randomByColumn(SQLColumnDefinition sqlColumnDefinition) {
        if (sqlColumnDefinition.isAutoIncrement()) {
            return 0;
        }
        if (sqlColumnDefinition.getOnUpdate() instanceof SQLCurrentTimeExpr) {
            return objectMapper.convertValue(new Date(), String.class);
        }
        if (sqlColumnDefinition.getDefaultExpr() instanceof SQLCurrentTimeExpr) {
            return objectMapper.convertValue(new Date(), String.class);
        }
        return null;
    }
}
