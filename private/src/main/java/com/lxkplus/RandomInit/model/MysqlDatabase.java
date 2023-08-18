package com.lxkplus.RandomInit.model;

public class MysqlDatabase {
    String userDataBaseName;

    public String getUserDataBaseName() {
        return userDataBaseName.split("_", 4)[3];
    }
}
