package com.lxkplus.RandomInit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.model.VO.MybatisParams;
import com.lxkplus.RandomInit.model.VO.TableParams;
import com.lxkplus.RandomInit.service.impl.CodeBuilderImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class MybatisGatherController {

    @Resource
    CodeBuilderImpl codeBuilder;

    @Resource
    ObjectMapper objectMapper;

    @PostMapping("/mybatisCode")
    public BodyResponse<Map<String, Object>> getMybatisPlusCode(
            @RequestBody TableParams tableParams,
            @RequestParam Map<String, Object> params) {
        MybatisParams mybatisParams =  objectMapper.convertValue(params, MybatisParams.class);

        log.info(mybatisParams.toString());
        String code = codeBuilder.mybatisPlusGather(tableParams, mybatisParams);
        return new BodyResponse<>(Map.of("code", code));
    }
}
