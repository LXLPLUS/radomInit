package com.lxkplus.RandomInit.enums;

public enum ErrorEnum {
    SUCCESS("正常执行", 0),
    NOT_HAVE_START_TIME("定时开始时间为空", -99),
    NOT_HAVE_FINISH_TIME("定时结束时间为空", -98),
    LENGTH_NOT_ENOUGH("列表长度不满足最小条件", -97),
    LENGTH_TOO_LONG("需要生成的数据过长", -96),
    REGEX_NOT_EXIST("正则解析失败", -95),
    NOT_HAVE_ANY_DATA("没有任何有效数据", -94),
    NOT_ENOUGH_PARAMS("缺乏必要参数", -93),
    NOT_HAVE_RULER("无法匹配任何规则", -92),
    NULL_ERROR("出现空指针异常", -91),
    PARAM_NOT_SUPPORT("对应参数不支持", -90),
    EMPTY("数据为空", -100),
    EXIST("对应数据存在且无法覆盖", -89),
    NOT_EXIST("查询的数据不存在!", -87),
    UNAUTHORIZED("未登录", -401),
    PASS_WORD_ERROR("密码错误", -402);


    public String errorName;
    public int index;

    ErrorEnum(String errorName, int index) {
        this.errorName = errorName;
        this.index = index;
    }
}
