package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.lxkplus.RandomInit.commons.CacheBuffer;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.MysqlEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.ColumnBuilderInfoMapper;
import com.lxkplus.RandomInit.mapper.PoolRegisterMapper;
import com.lxkplus.RandomInit.mapper.RulerRegisterMapper;
import com.lxkplus.RandomInit.model.DO.ColumnBuilderInfo;
import com.lxkplus.RandomInit.model.DO.PoolRegister;
import com.lxkplus.RandomInit.model.DO.RegexRegisterDo;
import com.lxkplus.RandomInit.model.VO.BuildRuler;
import com.lxkplus.RandomInit.model.VO.RegisterRulerVo;
import com.lxkplus.RandomInit.model.VO.SelectOption;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import com.lxkplus.RandomInit.service.RandomService;
import com.lxkplus.RandomInit.service.SchemaService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RandomServiceImpl implements RandomService {

    @Resource
    ObjectMapper objectMapper;

    @Resource
    SchemaService schemaService;
    
    @Resource
    Faker faker;

    @Resource
    MysqlCheckService mysqlCheckService;

    @Resource
    ColumnBuilderInfoMapper columnBuilderInfoMapper;

    @Resource
    RulerRegisterMapper rulerRegisterMapper;

    @Resource
    CacheBuffer cacheBuffer;

    @Resource
    PoolRegisterMapper poolRegisterMapper;


    @Override
    public boolean registerRuler(String actionID, String userSchemaName,
                                 String tableName, String columnName, String builderRuler,
                                 List<String> params) throws NormalErrorException {
        ColumnBuilderInfo columnBuilderInfo = new ColumnBuilderInfo();
        columnBuilderInfo.setActionID(actionID);
        columnBuilderInfo.setUserSchemaName(userSchemaName);
        columnBuilderInfo.setTableName(tableName);
        columnBuilderInfo.setColumnName(columnName);
        columnBuilderInfo.setBuilderRuler(builderRuler);
        columnBuilderInfo.setParams(StringUtils.join(params, " "));
        columnBuilderInfo.setInsertTime(new Date());

        boolean exist = mysqlCheckService.checkColumnExist(actionID, userSchemaName, tableName, columnName, false);
        if (!exist) {
            log.warn("因为对应列不存在，导致注入规则失败！{}", columnBuilderInfo);
            return false;
        }

        columnBuilderInfoMapper.insert(columnBuilderInfo);
        log.info("注入规则成功! {}", columnBuilderInfo);
        return true;
    }

    @Override
    public boolean registerRuler(String actionID, String userSchemaName,
                                 String tableName, String column, String builderRuler, String params) throws NormalErrorException {
        return registerRuler(actionID, userSchemaName, tableName, column, builderRuler, List.of(params));
    }

    @Override
    public boolean blindPool(String actionID, String userSchemaName, String tableName, String columnName, String poolName) throws NormalErrorException {
        mysqlCheckService.checkTableNameSafe(actionID, userSchemaName, tableName);
        mysqlCheckService.checkColumnExist(actionID, userSchemaName, tableName, columnName, true);
        String row = cacheBuffer.readFromPool(actionID, poolName);
        ThrowUtils.throwIfNullOrBlack(row, String.format("对应池%s不存在!", poolName));
        ColumnBuilderInfo columnBuilderInfo = new ColumnBuilderInfo();
        columnBuilderInfo.setActionID(actionID);
        columnBuilderInfo.setUserSchemaName(userSchemaName);
        columnBuilderInfo.setTableName(tableName);
        columnBuilderInfo.setColumnName(columnName);
        columnBuilderInfo.setPoolName(poolName);
        columnBuilderInfo.setInsertTime(new Date());

        columnBuilderInfoMapper.insert(columnBuilderInfo);
        log.info("列{} 成功绑定 pool {}", columnBuilderInfo, poolName);
        return true;
    }

    @Override
    public boolean registerRuler(String actionID, String userSchemaName,
                                 String tableName, String columnName, String builderRuler) throws NormalErrorException {
        return registerRuler(actionID, userSchemaName, tableName, columnName, builderRuler, Collections.emptyList());
    }

    @Override
    public ColumnBuilderInfo readRuler(String actionID, String userSchemaName, String tableName, String columnName,
                                       List<ColumnBuilderInfo> builderInfos) {
        List<ColumnBuilderInfo> collect = builderInfos.stream()
                .filter(x -> x.getActionID().equals(actionID))
                .filter(x -> x.getUserSchemaName().equals(userSchemaName))
                .filter(x -> x.getTableName().equals(tableName))
                .filter(x -> x.getColumnName().equals(columnName))
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            return null;
        }
        return collect.get(0);

    }

    @Override
    public ColumnBuilderInfo readRulerFromDB(String actionID, String userSchemaName, String tableName, String column) {
        List<ColumnBuilderInfo> columnBuilderInfos = columnBuilderInfoMapper.selectByMap(Map.of(
                "action_id", actionID,
                "user_database_name", userSchemaName,
                "table_name", tableName,
                "column_name", column));
        if (columnBuilderInfos == null || columnBuilderInfos.isEmpty()) {
            return null;
        }
        columnBuilderInfos.sort(Comparator.comparing(x -> -x.getInsertTime().getTime()));
        return columnBuilderInfos.get(0);
    }

    @Override
    public List<ColumnBuilderInfo> getRulerList(String actionID) {
        return columnBuilderInfoMapper.selectByMap(Map.of("action_id", actionID));
    }

    @Override
    public void clearRulers(String actionID) throws NormalErrorException {
        cacheBuffer.clearPool(actionID);
        columnBuilderInfoMapper.deleteByMap(Map.of("action_id", actionID));
    }

    @Override
    public Object randomByRuler(String buildRuler, List<String> params) throws NormalErrorException {
        if (StringUtils.isBlank(buildRuler)) {
            return null;
        }
        if (params == null) {
            params = Collections.emptyList();
        }
        switch (buildRuler){
            case "string":
            case "字符串":
            case "字母":
            case "varchar":
            case "char":
                if (params.isEmpty()) {
                    return RandomStringUtils.randomPrint(0, 20).replaceAll("['\"\\\\{}]", "");
                }
                else if (params.size() == 1) {
                    return RandomStringUtils.randomPrint(0, Integer.parseInt(params.get(0)))
                            .replaceAll("['\"\\\\{}]", "");
                }
                else {
                    return RandomStringUtils.randomPrint(Integer.parseInt(params.get(0)), Integer.parseInt(params.get(1)))
                            .replaceAll("['\"\\\\{}]", "");
                }
            case "string|param1":
                return RandomStringUtils.randomPrint(0, Integer.parseInt(params.get(0)))
                    .replaceAll("['\"\\\\{}]", "");
            case "string|param1|param2":
                return RandomStringUtils.randomPrint(Integer.parseInt(params.get(0)), Integer.parseInt(params.get(1)))
                        .replaceAll("['\"\\\\{}]", "");
            case "正则":
            case "正则表达式":
            case "regex":
                ThrowUtils.throwIf(params.isEmpty(), ErrorEnum.NotEnoughParams, "缺乏正则表达式参数");
                return faker.regexify(params.get(0));
            case "long":
            case "int":
            case "integer":
            case "数字":
            case "range":
            case "范围":
                if (params.isEmpty()) {
                    return faker.number().numberBetween(0, Integer.MAX_VALUE / 10);
                }
                else if (params.size() == 1) {
                    return faker.number().numberBetween(Long.parseLong(params.get(0)), Integer.MAX_VALUE / 2);
                }
                return faker.number().numberBetween(Long.parseLong(params.get(0)), Long.parseLong(params.get(1)));
            case "range|param1":
                return faker.number().numberBetween(Long.parseLong(params.get(0)), Integer.MAX_VALUE / 2);
            case "range|param1|param2":
                return faker.number().numberBetween(Long.parseLong(params.get(0)), Long.parseLong(params.get(1)));
            case "时间":
            case "time":
                Date startTime = DateUtils.addDays(new Date(), -100);
                Date endTime = DateUtils.addDays(new Date(), 100);
                try {
                    if (params.size() >= 1) {
                        startTime = DateUtils.parseDate(params.get(0), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
                    }
                    if (params.size() >= 2) {
                        endTime = DateUtils.parseDate(params.get(1), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return faker.date().between(startTime, endTime);

            case "time|param1":
                startTime = DateUtils.addDays(new Date(), -100);
                endTime = DateUtils.addDays(new Date(), 100);
                try {
                    startTime = DateUtils.parseDate(params.get(0), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return faker.date().between(startTime, endTime);
            case "正数":
                return faker.number().numberBetween(1, Integer.MAX_VALUE / 10);
            case "手机号":
                return faker.regexify("1[0-9]{10}");
            case "ip":
            case "ipv4":
                return faker.internet().ipV4Address();
            case "uuid":
                return faker.internet().uuid();
            case "url":
                return faker.internet().url();
            case "名字":
            case "name":
                return faker.name().name();
            case "default":
            case "默认值":
            case "枚举":
            case "enum":
                ThrowUtils.throwIf(params.isEmpty(), ErrorEnum.NotEnoughParams, "缺乏默认值");
                int index = faker.number().numberBetween(0, params.size());
                return params.get(index);
            case "金钱":
            case "钱":
            case "money":
                return faker.number().randomDouble(2, 0, 1000000);
            case "邮箱":
            case "mail":
            case "email":
                return faker.internet().emailAddress(faker.funnyName().name())
                        .replaceAll("\s+", "");
            default:
                return null;
        }
    }

    @Override
    public List<String> createPool(String actionID, String poolName,
                                   String buildRuler, List<String> params, int count) throws NormalErrorException {
        ThrowUtils.throwIf(count > 1e5, ErrorEnum.LengthTooLong, "一次生成的数据量超过最大限制10W");

        List<String> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Object o = randomByRuler(buildRuler, params);
            data.add(objectMapper.convertValue(o, String.class));
        }
        cacheBuffer.putIntoPool(actionID, poolName, data);

        PoolRegister poolRegister = new PoolRegister();
        poolRegister.setActionID(actionID);
        poolRegister.setPoolName(poolName);
        poolRegister.setRulerType(buildRuler);
        poolRegister.setInsertTime(new Date());
        poolRegister.setParams(StringUtils.join(params, ","));
        poolRegisterMapper.insert(poolRegister);

        return data;
    }

    @Override
    public Object randomByRuler(String actionID,
                                SQLColumnDefinition sqlColumnDefinition,
                                MySqlInsertStatement mySqlInsertStatement,
                                List<ColumnBuilderInfo> rulerList) throws NormalErrorException {
        String columnName = StringUtils.strip(sqlColumnDefinition.getColumnName(), "`");
        String tableName = StringUtils.strip(mySqlInsertStatement.getTableSource().getTableName(), "`");
        String schema = StringUtils.strip(mySqlInsertStatement.getTableSource().getSchema(), "`");

        String userSchemaName = schemaService.getFromRealSchemaName(schema).getRight();

        // 如果指定了数据源，那么就不用去查询数据库
        // 没有指定需要查询数据库
        ColumnBuilderInfo columnBuilderInfo;
        if (rulerList == null || rulerList.isEmpty()) {
            columnBuilderInfo = readRulerFromDB(actionID, userSchemaName, tableName, columnName);
        }
        else {
            columnBuilderInfo = readRuler(actionID, userSchemaName, tableName, columnName, rulerList);
        }

        if (columnBuilderInfo == null) {
            return null;
        }

        // 如果指定了一个pool,那就从pool里面取
        if (columnBuilderInfo.getPoolName() != null) {
            return cacheBuffer.readFromPool(actionID, columnBuilderInfo.getPoolName());
        }
        return randomByRuler(columnBuilderInfo.getBuilderRuler(), List.of(columnBuilderInfo.getParams().split("\\s+")));
    }

    @Override
    public Object randomByType(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException {
        MysqlEnum mysqlEnum = MysqlEnum.valueOf(sqlColumnDefinition.getDataType().getName().toUpperCase());
        List<SQLExpr> arguments = sqlColumnDefinition.getDataType().getArguments();
        switch (mysqlEnum) {
            case INT:
                return faker.number().numberBetween(0, Integer.MAX_VALUE / 10);
            case BIGINT:
            case LONG:
                return faker.number().numberBetween(0, Long.MAX_VALUE / 10);
            case DOUBLE:
                return faker.number().randomDouble(3, 0, Long.MAX_VALUE / 100);
            case FLOAT:
                return faker.number().randomDouble(3, 0, Integer.MAX_VALUE / 2);
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
                return faker.number().randomDouble(sqlExpr2,
                        0,
                        (long) Math.pow(10, sqlExpr1 - sqlExpr2) - 1);
            case TINYINT:
            case BOOL:
                return faker.number().numberBetween(0, 2);
            default:
                throw new NormalErrorException(ErrorEnum.paramNotSupport, String.format("%s这个参数不支持",
                        sqlColumnDefinition.getDataType().getName().toUpperCase()));
        }
    }

    @Override
    public Object randomByColumn(String actionID,
                                 SQLColumnDefinition sqlColumnDefinition,
                                 MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException {
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

    @Override
    public List<String> getDataByRegex(BuildRuler buildRuler) throws NormalErrorException {
        ThrowUtils.throwIfNullOrEmpty(buildRuler.getParams(), "缺乏必要参数");
        try {
            faker.regexify(buildRuler.getParams().get(0));
        } catch (Exception e) {
            throw new NormalErrorException(ErrorEnum.NotExist, "正则表达式不支持！");
        }
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < buildRuler.getCount(); i++) {
            ans.add(faker.regexify(buildRuler.getParams().get(0)));
            if (ans.get(i).length() > 1e5) {
                throw new NormalErrorException(ErrorEnum.LengthTooLong);
            }
        }
        return ans;
    }

    @Override
    public void registerRegexRuler(RegisterRulerVo registerRulerVo) throws NormalErrorException {

        // 检查规则是否已经注册
        List<RegexRegisterDo> regexRegisterDos = rulerRegisterMapper.selectByMap(Map.of("action_id", registerRulerVo.getActionID(),
                "params", registerRulerVo.getParams()));

        if (!regexRegisterDos.isEmpty()) {
            throw new NormalErrorException(ErrorEnum.Exist, "规则已经存在");
        }

        // 填入一次默认值
        BuildRuler buildRuler = new BuildRuler();
        buildRuler.setParams(List.of(registerRulerVo.getParams()));
        buildRuler.setCount(1);
        String example = getDataByRegex(buildRuler).get(0);

        // 写入数据库
        RegexRegisterDo regexRegisterDo = new RegexRegisterDo();
        regexRegisterDo.setActionID(registerRulerVo.getActionID());
        regexRegisterDo.setParams(registerRulerVo.getParams());
        regexRegisterDo.setInsertTime(new Date());
        regexRegisterDo.setRulerType(registerRulerVo.getRulerType());
        regexRegisterDo.setExample(example);
        rulerRegisterMapper.insert(regexRegisterDo);
    }


    @Override
    public List<String> testRuler(@NotNull BuildRuler buildRuler) throws NormalErrorException {
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < buildRuler.getCount(); i++) {
            ans.add(objectMapper.convertValue(randomByRuler(buildRuler.getRulerType(), buildRuler.getParams()), String.class));
        }
        return ans;
    }


    @Override
    public List<SelectOption> getRuler(@NotNull String actionID) {
        ArrayList<SelectOption> selectOptions = new ArrayList<>();
        List<RegexRegisterDo> rulerList = rulerRegisterMapper.selectByMap(Map.of("action_id", actionID));
        for (RegexRegisterDo regexRegisterDo : rulerList) {
            String label = String.format("%s|%s|%s", regexRegisterDo.getRulerType(),
                    StringUtils.substring(regexRegisterDo.getParams(), 0, 40),
                    regexRegisterDo.getExample());

            selectOptions.add(new SelectOption(label,
                    regexRegisterDo.getRulerType(),
                    List.of(regexRegisterDo.getParams().split("\n#"))));
        }
        return selectOptions;
    }

}
