package com.lxkplus.RandomInit.controller;

import com.lxkplus.RandomInit.commons.BodyResponse;
import com.lxkplus.RandomInit.dto.BuildRuler;
import com.lxkplus.RandomInit.dto.RegisterRulerVo;
import com.lxkplus.RandomInit.dto.SelectOption;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.service.RandomService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class RulerController {

    @Resource
    RandomService randomService;

    @Resource
    HttpSession httpSession;

    @PostMapping("/regex")
    public BodyResponse<List<String>> regexCheck(@RequestBody BuildRuler buildRuler) throws NormalErrorException {
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
        List<String> stringList = randomService.testRuler(buildRuler);
        return new BodyResponse<>(stringList);
    }

    @GetMapping("/userRuler")
    public BodyResponse<List<SelectOption>> getRuler() {
        String actionID = httpSession.getAttribute("actionID").toString();
        List<SelectOption> ruler = randomService.getRuler(actionID);
        return new BodyResponse<>(ruler);
    }
}
