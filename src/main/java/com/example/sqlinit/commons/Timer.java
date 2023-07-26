package com.example.sqlinit.commons;

import com.example.sqlinit.exception.ErrorEnum;
import com.example.sqlinit.exception.NormalErrorException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 一个简单的定时器，计算耗时用
 */
@Slf4j
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

    public long printSpendMillis() throws NormalErrorException {
        if (list[0] == 0) {
            throw new NormalErrorException(ErrorEnum.NotHaveStartTime);
        }
        if (list[1] == 0) {
            throw new NormalErrorException(ErrorEnum.NotHaveFinishTime);
        }
        log.info("耗时为{}毫秒", list[1] - list[0]);
        return list[1] - list[0];
    }
}
