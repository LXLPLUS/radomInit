package com.lxkplus.RandomInit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class DatabaseNameHelper {
    String userDatabaseName;
    String tableName;
    String alias;
    String sql;

    public String getOwner() {
        if (userDatabaseName == null && tableName != null) {
            return tableName;
        }
        if (userDatabaseName != null && tableName != null) {
            return userDatabaseName + "." + tableName;
        }
        return "undefined";
    }
}
