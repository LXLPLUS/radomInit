package com.lxkplus.RandomInit.utils;

import org.slf4j.helpers.MessageFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    static SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

    static public String format(String messagePattern, Object arg) {
        return MessageFormatter.format(messagePattern, arg).getMessage();
    }

    static public String format(String messagePattern, Object arg1, Object arg2) {
        return MessageFormatter.format(messagePattern, arg1, arg2).getMessage();
    }

    static public String format(String messagePattern, Object arg1, Object arg2, Object arg3) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg1, arg2, arg3}).getMessage();
    }

    static public String format(String messagePattern, Object arg1, Object arg2, Object arg3, Object arg4) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg1, arg2, arg3, arg4}).getMessage();
    }

    static public String formatToString(Object o) {
        // 不知道为什么，反正jackJson似乎无法进行类型转化，必然转化为时间戳
        if (o instanceof Date) {
            return ft.format(o);
        }
        return JsonUtils.objectMapper.convertValue(o, String.class);
    }
}
