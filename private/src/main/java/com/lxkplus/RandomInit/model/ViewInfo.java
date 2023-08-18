package com.lxkplus.RandomInit.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ViewInfo {
    String userDatabaseName;

    String tableName;
    String sql;

    public String getUserDatabaseName() {
        return userDatabaseName.split("_", 4)[3];
    }

    public String getSql() {
        return sql
                .replaceAll("randominit_temp_[^_]+_default", "")
                .replaceAll("randominit_temp_[^_]+_", "");
    }



}
