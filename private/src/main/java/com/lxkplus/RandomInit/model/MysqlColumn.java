package com.lxkplus.RandomInit.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MysqlColumn {
    String userDataBaseName;
    String tableName;
    String columnName;
    String dataType;
    String columnDefault;
    String extra;
    String comment;


    public String getUserDataBaseName() {
        return userDataBaseName.split("_", 4)[3];
    }
}
