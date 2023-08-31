package com.lxkplus.RandomInit.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MysqlTable {
    String userSchemaName;
    String tableName;
    String tableType;

    public String getUserSchemaName() {
        return userSchemaName.split("_", 4)[3];
    }
}