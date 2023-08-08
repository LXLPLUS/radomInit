package com.lxkplus.RandomInit.controller;

import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.DataMatrix;
import com.lxkplus.RandomInit.model.RandomParamList;
import com.lxkplus.RandomInit.service.impl.RandomServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomInitController {

    @Resource
    RandomServiceImpl randomService;

    @PostMapping("/fill/random")
    BodyResponse<DataMatrix> getRandomData(RandomParamList randomParamList) throws NormalErrorException {
        DataMatrix dataMatrix = randomService.fillRandomData(randomParamList);
        return new BodyResponse<>(dataMatrix, "随机数列表");
    }
}
