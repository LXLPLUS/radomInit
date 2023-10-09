package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.dto.BuildRuler;
import com.lxkplus.RandomInit.dto.RegisterRulerVo;
import com.lxkplus.RandomInit.dto.SelectOption;
import com.lxkplus.RandomInit.exception.NormalErrorException;

import java.util.List;

public interface RandomService {

    Object randomByRuler(String buildRuler, List<String> params) throws NormalErrorException;

    List<String> getDataByRegex(BuildRuler buildRuler) throws NormalErrorException;

    void registerRegexRuler(RegisterRulerVo registerRulerVo) throws NormalErrorException;

    List<String> testRuler(BuildRuler buildRuler) throws NormalErrorException;

    List<SelectOption> getRuler(String actionID);

}
