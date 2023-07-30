package com.lxkplus.RandomInit.commons;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;

import java.util.Arrays;

/**
 * 一个简单的定时器，计算耗时用
 */
public class Timer {
    long[] list = new long[2];

    public void flushStartTime() {
        list[0] = System.currentTimeMillis();
    }

    public void flushFinishTime() {
        list[1] = System.currentTimeMillis();
    }

    public void clear() {
        Arrays.fill(list, 0L);
    }

    public long getSpendMillis() throws NormalErrorException {
        if (list[0] == 0) {
            throw new NormalErrorException(ErrorEnum.NotHaveStartTime);
        }
        if (list[1] == 0) {
            throw new NormalErrorException(ErrorEnum.NotHaveFinishTime);
        }
        return list[1] - list[0];
    }
}
