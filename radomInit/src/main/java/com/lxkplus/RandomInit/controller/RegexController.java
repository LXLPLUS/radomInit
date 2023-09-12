package com.lxkplus.RandomInit.controller;

import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.VO.BuildRuler;
import com.lxkplus.RandomInit.model.VO.RegisterRulerVo;
import com.lxkplus.RandomInit.service.RandomService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class RegexController {

    @Resource
    RandomService randomService;

    @Resource
    HttpSession httpSession;

    @PostMapping("/regex")
    public BodyResponse<List<String>> regexCheck(@Valid @RequestBody BuildRuler buildRuler) throws NormalErrorException {
        return new BodyResponse<>(randomService.getDataByRegex(buildRuler));
    }

    @PostMapping("/registerRegex")
    public BodyResponse<Void> registerRuler(@Valid @RequestBody RegisterRulerVo registerRulerVo) throws NormalErrorException {
        Object actionID = httpSession.getAttribute("actionID");
        registerRulerVo.setActionID(actionID.toString());
        randomService.registerRegexRuler(registerRulerVo);
        return new BodyResponse<>(null);
    }

    @PostMapping("/builderRuler")
    public BodyResponse<List<String>> checkBuilderRuler(@Valid @RequestBody BuildRuler buildRuler) throws NormalErrorException {
        return new BodyResponse<>(null);
    }
}
