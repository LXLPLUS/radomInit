package com.lxkplus.RandomInit.exception;


import com.lxkplus.RandomInit.enums.ErrorEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ThrowUtils {

    /**
     * 通过某个条件校验是否异常
     * @param check 检查条件
     * @param errorEnum  异常枚举类
     * @param msg 信息
     * @throws NormalErrorException 抛出的异常
     */
    public static void throwIf(boolean check, ErrorEnum errorEnum, String msg) throws NormalErrorException {
        if (check) {
            throw new NormalErrorException(errorEnum, msg);
        }
    }

    /**
     * 检查是否是空指针，如果是空指针那么抛出异常
     * @param o 类
     * @param msg 异常信息
     * @throws NormalErrorException 抛出异常
     */
    public static void throwIfNull(Object o, String msg) throws NormalErrorException {
        if (Objects.isNull(o)) {
            throw new NormalErrorException(ErrorEnum.NULLError, msg);
        }
    }

    /**
     * 检查是否是空指针，如果是空指针或者空白那么抛出异常
     * @param o 类
     * @param msg 异常信息
     * @throws NormalErrorException 抛出异常
     */
    public static void throwIfNullOrBlack(Object o, String msg) throws NormalErrorException {
        if (o instanceof String str) {
            if (StringUtils.isBlank(str)) {
                throw new NormalErrorException(ErrorEnum.NULLError, msg);
            }
        }
        if (Objects.isNull(o)) {
            throw new NormalErrorException(ErrorEnum.NULLError, msg);
        }
    }

    /**
     * 检查是否是空指针，如果是空指针或者空列表那么抛出异常
     * @param msg 异常信息
     * @throws NormalErrorException 抛出异常
     */
    public static void throwIfNullOrEmpty(Iterable<Objects> iterable, String msg) throws NormalErrorException {
        if (Objects.isNull(iterable)) {
            throw new NormalErrorException(ErrorEnum.NULLError, msg);
        }
        if (!iterable.iterator().hasNext()) {
            throw new NormalErrorException(ErrorEnum.NULLError, msg);
        }
    }

    public static void throwIfTypeError(Object o, Class<?> type, String msg) throws NormalErrorException {
        if (o == null) {
            throwIfNull(null, "空指针异常");
        }
        if (!Objects.requireNonNull(o).getClass().isAssignableFrom(type)) {
            throw new NormalErrorException(ErrorEnum.paramNotSupport, msg);
        }
    }
}
