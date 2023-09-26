package com.lxkplus.RandomInit.commons;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.javafaker.Faker;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CacheBuffer {

    @Resource
    MysqlCheckService mysqlCheckService;

    static final String SEP = "_";

    @Resource
    Faker faker;

    ConcurrentMap<String, Long>  increaseCache;
    ConcurrentMap<String, Date>  timeCache;
    ConcurrentMap<String, LinkedHashSet<String>> poolCache;

    CacheBuffer() {

        Cache<String, Long> build = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
        increaseCache = build.asMap();

        Cache<String, Date> build2 = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();

        timeCache = build2.asMap();

        Cache<String, LinkedHashSet<String>> build3 = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100)
                .softValues()
                .build();

        poolCache = build3.asMap();

    }

    public long getIncrease(String actionID, String str, long startNumber, long minStep, long maxStep) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);

        str = actionID + SEP + str;
        long num = increaseCache.getOrDefault(str, startNumber - 1);
        increaseCache.put(str, num + faker.number().numberBetween(minStep, maxStep));
        return increaseCache.get(str);
    }

    public long getIncrease(String actionID, String str, long startNumber) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);

        str = actionID + SEP + str;
        Long num = increaseCache.getOrDefault(str, startNumber - 1);
        increaseCache.put(str, num + 1);
        return increaseCache.get(str);
    }

    public Date getTime(String actionID, String str, Date startTime, int minSecond, int maxSecond) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);

        str = actionID + SEP + str;
        Date time = DateUtils.addSeconds(timeCache.getOrDefault(str, startTime), faker.number().numberBetween(minSecond, maxSecond));
        timeCache.put(str, time);
        return timeCache.get(str);
    }

    public Date getTime(String actionID, String str, Date startTime) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);

        str = actionID + SEP + str;
        Date time = DateUtils.addSeconds(timeCache.getOrDefault(str, startTime), 60);
        timeCache.put(str, time);
        return timeCache.get(str);
    }

    public String createRandomPoolName(String actionID) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);

        String uuid = faker.internet().uuid().replace("_", "");
        String poolName = actionID + "_" + uuid;
        poolCache.put(poolName, new LinkedHashSet<>());
        return uuid;
    }

    public void putIntoPool(String actionID, String poolName, List<String> data) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);

        poolName = actionID + SEP + poolName;
        poolCache.computeIfAbsent(poolName, k -> new LinkedHashSet<>()).addAll(data);
    }

    public void putIntoPool(String actionID, String poolName, String data) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);

        poolName = actionID + SEP + poolName;
        poolCache.computeIfAbsent(poolName, k -> new LinkedHashSet<>()).add(data);
    }

    // 从池子里面获取数据的时候是随机的
    // 当获取完成以后，会形成循环
    public String readFromPool(String actionID, String poolName) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);

        poolName = actionID + SEP + poolName;
        if (!poolCache.containsKey(poolName)) {
            return null;
        }
        String topMessage = poolCache.get(poolName).iterator().next();
        poolCache.get(poolName).remove(topMessage);
        poolCache.get(poolName).add(topMessage);
        return topMessage;
    }

    public void clearPool(String actionID) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);

        ArrayList<String> strings = new ArrayList<>(poolCache.keySet());
        for (String string : strings) {
            if (string.startsWith(actionID + SEP)) {
                poolCache.remove(actionID + SEP);
            }
        }
    }

    public void logAllCache() {
        log.info("  =======   缓存信息   =======");
        increaseCache.keySet().forEach(log::info);
        timeCache.keySet().forEach(log::info);
        poolCache.keySet().forEach(log::info);
        log.info("  =======   缓存信息   =======");
    }
}
