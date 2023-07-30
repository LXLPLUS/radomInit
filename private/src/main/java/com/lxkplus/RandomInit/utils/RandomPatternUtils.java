package com.lxkplus.RandomInit.utils;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.HandlerEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.time.DateUtils;


import java.util.*;

public class RandomPatternUtils {

    static Faker faker = new Faker(Locale.CHINA);
    /**
     * 我到底为啥非要加那么多参数啊……自增索引真的好难实现，可是也只有自增需要那么复杂
     * 可变参数是什么鬼嘛，为什么非要引入这个
     * @param builderRuler 类型名称
     * @param count 一次性生成的数据数量
     * @param params 可变参数
     * @return 随机数列表
     */
    static public List<?> getByType(String builderRuler, int count, Map<String, Object> params)
            throws NormalErrorException {
        if (params == null) {
            params = new HashMap<>();
        }
        HandlerEnum handlerEnum = HandlerEnum.valueOf(builderRuler);
        switch (handlerEnum) {
            case regex:
                String regex = (String) params.get("regex");
                return randomByPattern(count, regex);
            case phoneNumber:
                return getPhoneNumber(count);
            case increaseID:
                int startIndex = (int) params.getOrDefault("startIndex", 0);
                return getIncreaseID(count, startIndex);
            case city:
                return getCity(count);
            case personName:
                return getPersonName(count);
            case randomInteger:
                int min = (int) params.getOrDefault("min", Integer.MIN_VALUE);
                int max = (int) params.getOrDefault("max", Integer.MAX_VALUE);
                boolean sorted = (boolean) params.getOrDefault("sorted", false);
                return randomInteger(count, min, max, sorted);
            case creditID:
                return getCreditID(count);
            case moneyNumber:
                return getMoney(count);
            case uuid:
                return randomUUID(count);
            case streetAddress:
                return getStreetAddress(count);
            case randomTime:
                sorted = (boolean) params.getOrDefault("sorted", false);
                Date startTime = (Date) params.getOrDefault("startTime", DateUtils.addYears(new Date(), -5));
                Date endTime = (Date) params.getOrDefault("endTime", DateUtils.addYears(new Date(), 5));
                return getRandomTime(count, startTime, endTime, sorted);
            case ip:
                return getIP(count);
            case email:
                return getEmail(count);
            case bool:
                return getBoolean(count);
            case passWord:
                return getPassword(count);
            case filePath:
                return getFilePath(count);
            default:
                throw new NormalErrorException(ErrorEnum.NotHaveRuler, builderRuler + "这个规则不存在");
        }

    }

    /**
     * 单独一个哈希表作为可变参数实现
     * @param params 参数
     * @return 随机数列表
     */
    static public List<?> getByType(Map<String, Object> params) throws NormalErrorException {
        String builderRuler = (String) params.get("builderRuler");
        int count = (int) params.get("count");
        return getByType(builderRuler, count, params);

    }
    static public List<String> randomByPattern(int count, String regex) throws NormalErrorException {
        List<String> randomString = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                randomString.add(faker.regexify(regex));
            } catch (IllegalArgumentException e) {
                throw new NormalErrorException(ErrorEnum.regexNotExist,
                        StringUtils.format("正则表达式{}解析失败！", regex));
            }
        }
        return randomString;
    }

    static public List<Integer> randomInteger(int count,int min, int max, Boolean sorted) throws NormalErrorException {

        if (min > max) {
            throw new NormalErrorException(ErrorEnum.LengthNotEnough, "数据范围错误");
        }
        ArrayList<Integer> randomInteger = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            randomInteger.add(faker.number().numberBetween(min, max));
        }

        if (sorted) {
            randomInteger.sort(Comparator.naturalOrder());
        }
        return randomInteger;
    }


    static public List<Integer> randomInteger(int count) throws NormalErrorException {
        return randomInteger(count, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
    }

    static public List<String> randomUUID(int count) {
        ArrayList<String> UUIDList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            UUIDList.add(faker.internet().uuid());
        }
        return UUIDList;
    }

    static public List<Long> getCreditID(int count) {
        ArrayList<Long> string = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            string.add(faker.number().randomNumber(18, true));
        }
        return string;
    }

    static public List<Integer> getIncreaseID(int count, int startIndex) {
        ArrayList<Integer> sortIntegerList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            sortIntegerList.add(i + startIndex);
        }
        return sortIntegerList;
    }

    static public List<Integer> getIncreaseID(int count) {
        return getIncreaseID(count, 0);
    }


    /**
     * 渭南, 太仓, 齐齐哈尔, 拉萨, 哈尔滨, 富阳, 宜昌, 蓬莱, 泰安, 西安
     *
     * @param count 数量
     * @return 城市列表
     */
    static public List<String> getCity(int count) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            strings.add(faker.address().cityName());
        }
        return strings;
    }

    /**
     * 街道信息 杨桥19151号 江街393号
     *
     * @param count 数量
     * @return 街道信息
     */
    static public List<String> getStreetAddress(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.address().streetAddress());
        }
        return strings;
    }

    static public List<Date> getRandomTime(int count, Date startTime, Date endTime, boolean sorted) {
        ArrayList<Date> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.date().between(startTime, endTime));
        }
        if (sorted) {
            strings.sort(Date::compareTo);
        }
        return strings;
    }

    static public List<Date> getRandomTime(int count) {
        return getRandomTime(count, DateUtils.addYears(new Date(), -5), DateUtils.addYears(new Date(), 5), false);
    }

    static public List<String> getPersonName(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.name().name());
        }
        return strings;
    }

    static public List<String> getUniversity(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.university().name());
        }
        return strings;
    }

    static public List<Boolean> getBoolean(int count) {
        ArrayList<Boolean> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.bool().bool());
        }
        return strings;
    }

    static public List<Long> getPhoneNumber(int count) {
        ArrayList<Long> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(Long.parseLong(faker.regexify("1[0-9]{10}")));
        }
        return strings;
    }

    static public List<Double> getMoney(int count) {
        ArrayList<Double> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.number().randomDouble(2, 0, 100000));
        }
        return strings;
    }

    static public List<String> getIP(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.internet().ipV4Address());
        }
        return strings;
    }

    static public List<String> getEmail(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String head = faker.internet().password(5, 12).replace("@", "");
            strings.add(faker.internet().emailAddress(head));
        }
        return strings;
    }

    static public List<String> getFilePath(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.file().fileName());
        }
        return strings;
    }

    static public List<String> getPassword(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.internet().password(12, 24, true));
        }
        return strings;
    }
}
