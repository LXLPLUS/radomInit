package com.lxkplus.RandomInit.commons;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.javafaker.Faker;
import com.lxkplus.RandomInit.exception.NormalErrorException;
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

    static final String SEP = "_";

    @Resource
    Faker faker;

    ConcurrentMap<String, Object>  cache;
    ConcurrentMap<String, LinkedHashSet<String>> poolCache;

    CacheBuffer() {

        Cache<String, Object> build = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
        cache = build.asMap();


        Cache<String, LinkedHashSet<String>> build3 = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100)
                .softValues()
                .build();

    }

    public long getIncrease(String actionID, String str, long startNumber, long minStep, long maxStep) throws NormalErrorException {
        str = actionID + SEP + str;
        long num = (Long) cache.getOrDefault(str, startNumber - 1);
        cache.put(str, num + faker.number().numberBetween(minStep, maxStep));
        return (Long) cache.get(str);
    }

    public long getIncrease(String actionID, String str, long startNumber) throws NormalErrorException {

        str = actionID + SEP + str;
        Long num = (Long) cache.getOrDefault(str, startNumber - 1);
        cache.put(str, num + 1);
        return (Long) cache.get(str);
    }

    public Date getTime(String actionID, String str, Date startTime, int minSecond, int maxSecond) throws NormalErrorException {
        str = actionID + SEP + str;
        Date time = DateUtils.addSeconds((Date) cache.getOrDefault(str, startTime), faker.number().numberBetween(minSecond, maxSecond));
        cache.put(str, time);
        return (Date) cache.get(str);
    }

    public Date getTime(String actionID, String str, Date startTime) throws NormalErrorException {
        str = actionID + SEP + str;
        Date time = DateUtils.addSeconds((Date) cache.getOrDefault(str, startTime), 60);
        cache.put(str, time);
        return (Date) cache.get(str);
    }

    public String createRandomPoolName(String actionID) throws NormalErrorException {
        String uuid = faker.internet().uuid().replace("_", "");
        String poolName = actionID + "_" + uuid;
        poolCache.put(poolName, new LinkedHashSet<>());
        return uuid;
    }

    public void putIntoPool(String actionID, String poolName, List<String> data) throws NormalErrorException {
        poolName = actionID + SEP + poolName;
        poolCache.computeIfAbsent(poolName, k -> new LinkedHashSet<>()).addAll(data);
    }

    public void putIntoPool(String actionID, String poolName, String data) throws NormalErrorException {
        poolName = actionID + SEP + poolName;
        poolCache.computeIfAbsent(poolName, k -> new LinkedHashSet<>()).add(data);
    }

    // 从池子里面获取数据的时候是随机的
    // 当获取完成以后，会形成循环
    public String readFromPool(String actionID, String poolName) throws NormalErrorException {
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
        ArrayList<String> strings = new ArrayList<>(poolCache.keySet());
        for (String string : strings) {
            if (string.startsWith(actionID + SEP)) {
                poolCache.remove(actionID + SEP);
            }
        }
    }

    public void put(String actionID, String name, Object data) {
        name = actionID + SEP + name;
        cache.put(name, data);
    }

    public Object get(String actionID, String name) {
        return cache.getOrDefault(actionID + SEP + name, null);
    }


    public void logAllCache() {
        log.info("  =======   缓存信息   =======");
        cache.keySet().forEach(log::info);
        poolCache.keySet().forEach(log::info);
        log.info("  =======   缓存信息   =======");
    }
}
