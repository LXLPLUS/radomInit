package com.lxkplus.RandomInit.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Getter
public class RandomParamList {
    List<RandomParam> paramsList = new ArrayList<>();
    public Timer timer;

    public void add(RandomParam randomParam) {
        paramsList.add(randomParam);
    }
}

