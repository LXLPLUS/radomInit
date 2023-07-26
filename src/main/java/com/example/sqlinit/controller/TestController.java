package com.example.sqlinit.controller;

import com.example.sqlinit.exception.BodyResponse;
import com.example.sqlinit.exception.ErrorEnum;
import com.example.sqlinit.exception.NormalErrorException;
import com.example.sqlinit.utils.RandomPatternUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @PostMapping("except")
    public int testException() throws NormalErrorException {
        throw new NormalErrorException(ErrorEnum.LengthNotEnough, "测试原因");
    }

    @PostMapping("except/1/")
    public BodyResponse<List<String>> testException2() throws NormalErrorException {
        return new BodyResponse<>(RandomPatternUtils.randomByPattern("[a-z]{5,7}", 10), "list");
    }

}
