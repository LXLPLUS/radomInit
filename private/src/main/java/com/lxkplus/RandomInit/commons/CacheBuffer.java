package com.lxkplus.RandomInit.commons;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lxkplus.RandomInit.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Component
public class CacheBuffer {

    @Resource
    JsonUtils jsonUtils;

    final String sep = "%";

    ConcurrentMap<String, Long>  increaseCache;
    ConcurrentMap<String, Date>  timeCache;

    CacheBuffer() {

        Cache<String, Long> build = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
        increaseCache = build.asMap();

        Cache<String, Date> build2 = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();

        timeCache = build2.asMap();
    }

    public long getIncrease(String actionID, String str, long startNumber, long minStep, long maxStep) {
        str = actionID + sep + str;
        long num = increaseCache.getOrDefault(str, startNumber - 1);
        increaseCache.put(str, num + jsonUtils.faker.number().numberBetween(minStep, maxStep));
        return increaseCache.get(str);
    }

    public long getIncrease(String actionID, String str, long startNumber) {
        str = actionID + sep + str;
        Long num = increaseCache.getOrDefault(str, startNumber - 1);
        increaseCache.put(str, num + 1);
        return increaseCache.get(str);
    }

    public Date getTime(String actionID, String str, Date startTime, int minSecond, int maxSecond) {
        str = actionID + sep + str;
        Date time = DateUtils.addSeconds(timeCache.getOrDefault(str, startTime), jsonUtils.faker.number().numberBetween(minSecond, maxSecond));
        timeCache.put(str, time);
        return timeCache.get(str);
    }

    public Date getTime(String actionID, String str, Date startTime) {
        str = actionID + sep + str;
        Date time = DateUtils.addSeconds(timeCache.getOrDefault(str, startTime), 60);
        timeCache.put(str, time);
        return timeCache.get(str);
    }
}
