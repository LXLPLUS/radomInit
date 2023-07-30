package com.lxkplus.RandomInit.enums;

public enum HandlerEnum {
    regex("正则表达式"),
    phoneNumber("电话号码"),
    randomInteger("随机数"),
    increaseID("自增数字"),
    personName("人名"),
    city("地址"),
    creditID("身份证"),
    moneyNumber("钱数"),
    uuid("uuid"),
    streetAddress("街道"),
    randomTime("随机时间"),
    ip("ip"),
    bool("true、false"),
    email("email"),
    filePath("文件路径"),
    passWord("账号密码");

    String ruler;

    HandlerEnum(String ruler) {
        this.ruler = ruler;
    }
}
