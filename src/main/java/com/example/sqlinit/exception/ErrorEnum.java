package com.example.sqlinit.exception;

public enum ErrorEnum {
    Success("正常执行", 0),
    NotHaveStartTime("定时开始时间", -99),
    NotHaveFinishTime("定时结束时间", -98),
    LengthNotEnough("列表长度不满足最小条件", -97),
    LengthTooLong("需要生成的数据过长", -96),
    regexNotExist("正则解析失败", -95);


    String errorName;
    int index;

    ErrorEnum(String errorName, int index) {
        this.errorName = errorName;
        this.index = index;
    }
}
