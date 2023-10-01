package com.lxkplus.RandomInit.controller;

import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.model.VO.FreeMakerVO;
import com.lxkplus.RandomInit.service.impl.FreeMakerServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FreeMakerController {

    @Resource
    FreeMakerServiceImpl freeMakerService;

    @Resource
    HttpSession httpSession;


    @PostMapping("/templateParamsGather")
    public BodyResponse<String> getDataByTemplate(@RequestBody FreeMakerVO freeMakerVO) {
        String data = freeMakerService.fillTemplate(freeMakerVO.getTemplate(),
                (String) httpSession.getAttribute("actionID"));
        return new BodyResponse<>(data);
    }
}
