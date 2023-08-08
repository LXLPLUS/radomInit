package com.lxkplus.RandomInit.controller;

import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.commons.Timer;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.DataMatrix;
import com.lxkplus.RandomInit.service.impl.RandomServiceImpl;
import com.lxkplus.RandomInit.utils.RandomDataMaker;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Resource
    RandomServiceImpl randomService;

    @Resource
    RandomDataMaker randomDataMaker;


    @PostMapping("except")
    public int testException() throws NormalErrorException {
        throw new NormalErrorException(ErrorEnum.LengthNotEnough, "测试原因");
    }

    @PostMapping("except/1/")
    public BodyResponse<List<String>> testException2() throws NormalErrorException {
        return new BodyResponse<>(randomDataMaker.randomByPattern(10, "[a-z]{5,7}"), "list");
    }

    @PostMapping("except/2/")
    public BodyResponse<DataMatrix> testException3() throws NormalErrorException {
        Timer timer = new Timer();
        timer.flushStartTime();
        DataMatrix dataMatrix = new DataMatrix();
        dataMatrix.addColumn("row1", randomDataMaker.getCreditID(10));
        dataMatrix.addColumn("row2", randomDataMaker.getCity(10));
        dataMatrix.addColumn("row3", randomDataMaker.randomUUID(10));
        dataMatrix.addColumn("row4", randomDataMaker.randomByPattern(10, "[0-9]{12,14}"));
        dataMatrix.addColumn("row5", randomDataMaker.getPhoneNumber(10));
        dataMatrix.addColumn("row6", randomDataMaker.getBoolean(10));
        timer.flushFinishTime();
        return new BodyResponse<>(dataMatrix, String.format("耗费的时间为%dms", timer.getSpendMillis()));
    }

}
