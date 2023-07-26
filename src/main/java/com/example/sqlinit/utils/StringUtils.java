package com.example.sqlinit.utils;

import org.slf4j.helpers.MessageFormatter;

public class StringUtils {
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
}
