package com.lxkplus.RandomInit.commons;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;

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

    public long getSpendMillis() throws NormalErrorException {
        if (list[0] == 0) {
            throw new NormalErrorException(ErrorEnum.NOT_HAVE_FINISH_TIME);
        }
        if (list[1] == 0) {
            throw new NormalErrorException(ErrorEnum.NOT_HAVE_FINISH_TIME);
        }
        return list[1] - list[0];
    }
}
