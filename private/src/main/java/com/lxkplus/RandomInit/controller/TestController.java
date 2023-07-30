package com.lxkplus.RandomInit.controller;

import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.commons.Timer;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.DataMatrix;
import com.lxkplus.RandomInit.utils.ExcelUtils;
import com.lxkplus.RandomInit.utils.RandomPatternUtils;
import com.lxkplus.RandomInit.utils.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class TestController {


    @PostMapping("except")
    public int testException() throws NormalErrorException {
        throw new NormalErrorException(ErrorEnum.LengthNotEnough, "测试原因");
    }

    @PostMapping("except/1/")
    public BodyResponse<List<String>> testException2() throws NormalErrorException {
        return new BodyResponse<>(RandomPatternUtils.randomByPattern(10, "[a-z]{5,7}"), "list");
    }

    @PostMapping("except/2/")
    public BodyResponse<DataMatrix> testException3() throws NormalErrorException, IOException {
        Timer timer = new Timer();
        timer.flushStartTime();
        DataMatrix dataMatrix = new DataMatrix();
        dataMatrix.addColumn("row1", RandomPatternUtils.getCreditID(10));
        dataMatrix.addColumn("row2", RandomPatternUtils.getCity(10));
        dataMatrix.addColumn("row3", RandomPatternUtils.randomUUID(10));
        dataMatrix.addColumn("row4", RandomPatternUtils.randomByPattern(10, "[0-9]{12,14}"));
        dataMatrix.addColumn("row5", RandomPatternUtils.getPhoneNumber(10));
        dataMatrix.addColumn("row6", RandomPatternUtils.getBoolean(10));
        dataMatrix.addColumn("row7", RandomPatternUtils.getRandomTime(10));
        timer.flushFinishTime();
        ExcelUtils.excelMaker(dataMatrix.toMatrix(), "1.xlsx");
        return new BodyResponse<>(dataMatrix, StringUtils.format("耗费的时间为{}ms", timer.getSpendMillis()));
    }

}
