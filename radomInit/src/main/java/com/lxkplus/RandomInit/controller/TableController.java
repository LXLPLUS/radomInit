package com.lxkplus.RandomInit.controller;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.model.VO.TableParams;
import com.lxkplus.RandomInit.service.TableService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class TableController {

    @Resource
    TableService tableService;

    @PostMapping("/gatherSql")
    @ResponseBody
    public BodyResponse<Map<String, String>> regexCheck(@Valid @RequestBody TableParams tableParams) {
        MySqlCreateTableStatement statement = tableService.convertVoToStatement(tableParams);
        return new BodyResponse<>(Map.of("sql", statement.toString()));
    }
}
