package com.lxkplus.RandomInit.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MysqlTable {
    String namespaceName;
    String tableName;
    String tableType;

    public String getNamespaceName() {
        return namespaceName.split("_", 4)[3];
    }
}