package com.lxkplus.RandomInit.model;

public class MysqlSchema {
    String userSchemaName;

    public String getUserSchemaName() {
        return userSchemaName.split("_", 4)[3];
    }
}
