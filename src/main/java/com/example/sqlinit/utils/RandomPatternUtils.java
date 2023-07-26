package com.example.sqlinit.utils;
import com.example.sqlinit.commons.Env;
import com.example.sqlinit.exception.ErrorEnum;
import com.example.sqlinit.exception.NormalErrorException;
import com.mifmif.common.regex.Generex;


import java.util.*;

public class RandomPatternUtils {

    static Random r = new Random();

    static private void checkCount(int count) throws NormalErrorException {
        if (count <= 0) {
            throw new NormalErrorException(ErrorEnum.LengthNotEnough,
                    StringUtils.format("获取的最大长度为{}, 小于等于0", count));
        }

        if (count >= Env.maxLen) {
            throw new NormalErrorException(ErrorEnum.LengthTooLong);
        }
    }

    static public List<String> randomByPattern(String regex, int count) throws NormalErrorException {
        checkCount(count);
        Generex generex;
        try {
           generex = new Generex(regex);
        } catch (IllegalArgumentException e) {
            throw new NormalErrorException(ErrorEnum.regexNotExist,
                    StringUtils.format("正则表达式{}解析失败！, {}", regex, e.getMessage()));
        }

        List<String> randomString = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            randomString.add(generex.random());
        }
        return randomString;
    }

    static public List<Integer> randomInteger(int count, Boolean sorted) throws NormalErrorException {
        checkCount(count);

        ArrayList<Integer> randomInteger = new ArrayList<>(count);

        randomInteger.add(r.nextInt());

        if (sorted) {
            randomInteger.sort(Comparator.naturalOrder());
        }
        return randomInteger;
    }


    static public List<Integer> randomInteger(int count) throws NormalErrorException {
        return randomInteger(count, false);
    }

    static public List<String> randomUUID(int count) throws NormalErrorException {
        checkCount(count);

        ArrayList<String> UUIDList = new ArrayList<>(count);
        UUIDList.add(UUID.randomUUID().toString());

        return UUIDList;
    }

    static public List<Integer> increaseInteger(int count, int startIndex) throws NormalErrorException {
        checkCount(count);

        ArrayList<Integer> sortIntegerList = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            sortIntegerList.add(i + startIndex);
        }

        return sortIntegerList;
    }

    static public List<Integer> increaseInteger(int count) throws NormalErrorException {
        return increaseInteger(count, 0);
    }
}
