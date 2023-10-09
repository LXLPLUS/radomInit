package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.lxkplus.RandomInit.commons.CacheBuffer;
import com.lxkplus.RandomInit.dto.BuildRuler;
import com.lxkplus.RandomInit.dto.RegisterRulerVo;
import com.lxkplus.RandomInit.dto.SelectOption;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.MysqlEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.RulerRegisterMapper;
import com.lxkplus.RandomInit.model.entity.RegexRegisterDo;
import com.lxkplus.RandomInit.service.RandomService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
public class RandomServiceImpl implements RandomService {

    @Resource
    ObjectMapper objectMapper;
    
    @Resource
    Faker faker;

    @Resource
    RulerRegisterMapper rulerRegisterMapper;

    @Resource
    CacheBuffer cacheBuffer;

    @Override
    public Object randomByRuler(String buildRuler, List<String> params) throws NormalErrorException {
        if (StringUtils.isBlank(buildRuler)) {
            return null;
        }
        if (params == null) {
            params = Collections.emptyList();
        }
        switch (buildRuler){
            case "string", "字符串", "字母", "varchar", "char":
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
            case "正则", "正则表达式", "regex":
                ThrowUtils.throwIf(params.isEmpty(), ErrorEnum.NOT_ENOUGH_PARAMS, "缺乏正则表达式参数");
                return faker.regexify(params.get(0));
            case "long", "int", "integer", "数字", "range", "范围":
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
            case "时间", "time":
                Date startTime = DateUtils.addDays(new Date(), -100);
                Date endTime = DateUtils.addDays(new Date(), 100);
                try {
                    if (!params.isEmpty()) {
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
            case "ip", "ipv4":
                return faker.internet().ipV4Address();
            case "uuid":
                return faker.internet().uuid();
            case "url":
                return faker.internet().url();
            case "名字", "name":
                return faker.name().name();
            case "default", "默认值", "枚举", "enum":
                ThrowUtils.throwIf(params.isEmpty(), ErrorEnum.NOT_ENOUGH_PARAMS, "缺乏默认值");
                int index = faker.number().numberBetween(0, params.size());
                return params.get(index);
            case "金钱", "钱", "money":
                return faker.number().randomDouble(2, 0, 1000000);
            case "邮箱", "mail", "email":
                return faker.internet().emailAddress(faker.funnyName().name())
                        .replaceAll("\s+", "");
            default:
                return null;
        }
    }

    public Object randomByType(String actionID, SQLColumnDefinition sqlColumnDefinition, MySqlInsertStatement mySqlInsertStatement) throws NormalErrorException {
        MysqlEnum mysqlEnum = MysqlEnum.valueOf(sqlColumnDefinition.getDataType().getName().toUpperCase());
        List<SQLExpr> arguments = sqlColumnDefinition.getDataType().getArguments();
        switch (mysqlEnum) {
            case INT:
                return faker.number().numberBetween(0, Integer.MAX_VALUE / 10);
            case BIGINT, LONG:
                return faker.number().numberBetween(0, Long.MAX_VALUE / 10);
            case DOUBLE:
                return faker.number().randomDouble(3, 0, Long.MAX_VALUE / 100);
            case FLOAT:
                return faker.number().randomDouble(3, 0, Integer.MAX_VALUE / 2);
            case VARCHAR:
                int sqlExpr = ((SQLIntegerExpr) arguments.get(0)).getNumber().intValue() - 10;
                sqlExpr = Math.max(sqlExpr, 2);
                return RandomStringUtils.randomPrint(0, sqlExpr).replaceAll("['\"\\\\{}]", "");
            case DATETIME, DATE, TIMESTAMP:
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
                        (long) Math.pow(10, (double) sqlExpr1 - sqlExpr2) - 1);
            case TINYINT, BOOL:
                return faker.number().numberBetween(0, 2);
            default:
                throw new NormalErrorException(ErrorEnum.PARAM_NOT_SUPPORT, String.format("%s这个参数不支持",
                        sqlColumnDefinition.getDataType().getName().toUpperCase()));
        }
    }

    @Override
    public List<String> getDataByRegex(BuildRuler buildRuler) throws NormalErrorException {
        ThrowUtils.throwIfNullOrEmpty(buildRuler.getParams(), "缺乏必要参数");
        try {
            faker.regexify(buildRuler.getParams().get(0));
        } catch (Exception e) {
            throw new NormalErrorException(ErrorEnum.NOT_EXIST, "正则表达式不支持！");
        }
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < buildRuler.getCount(); i++) {
            ans.add(faker.regexify(buildRuler.getParams().get(0)));
            if (ans.get(i).length() > 1e5) {
                throw new NormalErrorException(ErrorEnum.LENGTH_TOO_LONG);
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
            throw new NormalErrorException(ErrorEnum.EXIST, "规则已经存在");
        }

        // 填入一次默认值
        BuildRuler buildRuler =  new BuildRuler();
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
                    List.of(regexRegisterDo.getParams().split("\n##"))));
        }
        return selectOptions;
    }

}
