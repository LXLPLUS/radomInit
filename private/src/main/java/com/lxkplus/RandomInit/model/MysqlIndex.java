package com.lxkplus.RandomInit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MysqlIndex {
    String indexType;
    String indexName;
    List<String> rowList = new ArrayList<>();

    public String getIndexName() {
        return StringUtils.strip(indexName, "`");
    }

    public List<String> getRowList() {
        ArrayList<String> strings = new ArrayList<>(rowList);
        for (int i = 0; i < strings.size(); i++) {
            String strip = StringUtils.strip(strings.get(i), "`");
            strings.set(i, "`" + strip + "`");
        }
        return strings;
    }
}
