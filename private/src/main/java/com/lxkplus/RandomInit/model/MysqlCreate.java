package com.lxkplus.RandomInit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lxkplus.RandomInit.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
public class MysqlCreate {

    @JsonIgnore
    @Resource
    JsonUtils jsonUtils;

    String database;
    String tableName = "";
    String engine = "InnoDB";
    String charSet = "utf8mb4";
    String comment = "";
    String Collate = "utf8mb4_0900_ai_ci";
    List<MysqlColumn> mysqlColumnDefinitionList = new ArrayList<>();
    List<MysqlIndex> mysqlIndexList = new ArrayList<>();

    public String getTableName() {
        if (StringUtils.isBlank(tableName)) {
            tableName = jsonUtils.faker.pokemon().name();
        }
        tableName = StringEscapeUtils.escapeHtml4(tableName);
        return "`" + tableName + "`";
    }

    public String getComment() {
        return "'" + comment + "'";
    }

    public void setTableName(String tableName) {
        this.tableName = StringUtils.strip(tableName, "`");
    }
}