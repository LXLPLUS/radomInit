package com.lxkplus.RandomInit.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ViewInfo {
    String userSchemaName;
    String tableName;
    String sql;

    public String getuserSchemaName() {
        return userSchemaName.split("_", 4)[3];
    }

    public String getSql() {
        return sql
                .replaceAll("randominit_temp_[^_]+_default", "")
                .replaceAll("randominit_temp_[^_]+_", "");
    }



}
