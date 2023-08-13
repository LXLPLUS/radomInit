package com.lxkplus.RandomInit.enums;

public enum ErrorEnum {
    Success("正常执行", 0),
    NotHaveStartTime("定时开始时间为空", -99),
    NotHaveFinishTime("定时结束时间为空", -98),
    LengthNotEnough("列表长度不满足最小条件", -97),
    LengthTooLong("需要生成的数据过长", -96),
    regexNotExist("正则解析失败", -95),
    NotHaveAnyData("没有任何有效数据", -94),
    NotEnoughParams("缺乏必要参数", -93),
    NotHaveRuler("无法匹配任何规则", -92),
    NULLError("出现空指针异常", -91),
    paramNotSupport("对应参数不支持", -90),
    Empty("数据为空", -100),
    Exist("对应数据存在且无法覆盖", -89);


    public String errorName;
    public int index;

    ErrorEnum(String errorName, int index) {
        this.errorName = errorName;
        this.index = index;
    }
}
