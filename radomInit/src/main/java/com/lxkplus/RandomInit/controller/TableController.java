package com.lxkplus.RandomInit.controller;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.dto.TableMessage;
import com.lxkplus.RandomInit.dto.TableParams;
import com.lxkplus.RandomInit.dto.TestDDL;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.service.CreateSqlService;
import com.lxkplus.RandomInit.service.TableService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class TableController {

    @Resource
    TableService tableService;

    @Resource
    CreateSqlService createSqlService;


    @PostMapping("/gatherSql")
    @ResponseBody
    public BodyResponse<Map<String, String>> gatherSql(@Valid @RequestBody TableParams tableParams) {
        MySqlCreateTableStatement statement = tableService.convertVoToStatement(tableParams);
        return new BodyResponse<>(Map.of("sql", statement.toString()));
    }

    @PostMapping("/explainSql")
    @ResponseBody
    public BodyResponse<TableParams> regexCheck(@Valid @RequestBody Map<String, String> sqlMap) throws NormalErrorException {
        return new BodyResponse<>(tableService.convertStatToVo(sqlMap.getOrDefault("sql", "")));
    }

    @PostMapping("/registerDDL")
    @ResponseBody
    public BodyResponse<TestDDL> registerDDL(@Valid @RequestBody Map<String, String> sqlMap) {
        return new BodyResponse<>(createSqlService.testThenSaveDDL(sqlMap.getOrDefault("sql", "")));
    }

    @GetMapping("/tableList")
    public BodyResponse<List<TableMessage>> tableList() {
        return new BodyResponse<>(createSqlService.getDataByActionID());
    }
}
