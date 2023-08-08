package com.lxkplus.RandomInit.utils;

import com.github.javafaker.Faker;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RandomDataMaker {

    static Faker faker = new Faker(Locale.CHINA);
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<String> randomByPattern(int count, String regex) throws NormalErrorException {
        List<String> randomString = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                randomString.add(faker.regexify(regex));
            } catch (IllegalArgumentException e) {
                throw new NormalErrorException(ErrorEnum.regexNotExist,
                        String.format("正则表达式%s解析失败！", regex));
            }
        }
        return randomString;
    }

    public List<String> randomInteger(int count,int min, int max, Boolean sorted) throws NormalErrorException {

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
        return randomInteger.stream().map(Object::toString).collect(Collectors.toList());
    }

    public List<String> randomUUID(int count) {
        ArrayList<String> UUIDList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            UUIDList.add(faker.internet().uuid());
        }
        return UUIDList;
    }

    public List<String> getCreditID(int count) {
        ArrayList<Long> string = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            string.add(faker.number().randomNumber(18, true));
        }
        return string.stream().map(Objects::toString).collect(Collectors.toList());
    }

    public List<String> getIncreaseID(int count, int startIndex) {
        ArrayList<Integer> sortIntegerList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            sortIntegerList.add(i + startIndex);
        }
        return sortIntegerList.stream().map(Object::toString).collect(Collectors.toList());
    }


    /**
     * 渭南, 太仓, 齐齐哈尔, 拉萨, 哈尔滨, 富阳, 宜昌, 蓬莱, 泰安, 西安
     *
     * @param count 数量
     * @return 城市列表
     */
    public List<String> getCity(int count) {
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
    public List<String> getStreetAddress(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.address().streetAddress());
        }
        return strings;
    }

    public List<String> getRandomTime(int count, Date startTime, Date endTime, boolean sorted) {
        ArrayList<Date> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.date().between(startTime, endTime));
        }
        if (sorted) {
            strings.sort(Date::compareTo);
        }
        return strings.stream().map(simpleDateFormat::format).collect(Collectors.toList());
    }

    public List<String> getPersonName(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.name().name());
        }
        return strings;
    }

    public List<String> getUniversity(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.university().name());
        }
        return strings;
    }

    public List<String> getBoolean(int count) {
        ArrayList<Boolean> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.bool().bool());
        }
        return strings.stream().map(Object::toString).collect(Collectors.toList());
    }

    public List<String> getPhoneNumber(int count) {
        ArrayList<Long> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(Long.parseLong(faker.regexify("1[0-9]{10}")));
        }
        return strings.stream().map(Object::toString).collect(Collectors.toList());
    }

    public List<String> getMoney(int count) {
        ArrayList<Double> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.number().randomDouble(2, 0, 100000));
        }
        return strings.stream().map(Object::toString).collect(Collectors.toList());
    }

    public List<String> getIP(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.internet().ipV4Address());
        }
        return strings;
    }

    public List<String> getEmail(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String head = faker.internet().password(5, 12).replace("@", "");
            strings.add(faker.internet().emailAddress(head));
        }
        return strings;
    }

    public List<String> getFilePath(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.file().fileName());
        }
        return strings;
    }

    public List<String> getPassword(int count) {
        ArrayList<String> strings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            strings.add(faker.internet().password(12, 24, true));
        }
        return strings;
    }
}
