package com.lxkplus.RandomInit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class DatabaseNameHelper {
    String userSchemaName;
    String tableName;
    String alias;
    String sql;

    public String getOwner() {
        if (userSchemaName == null && tableName != null) {
            return tableName;
        }
        if (userSchemaName != null && tableName != null) {
            return userSchemaName + "." + tableName;
        }
        return "undefined";
    }
}
