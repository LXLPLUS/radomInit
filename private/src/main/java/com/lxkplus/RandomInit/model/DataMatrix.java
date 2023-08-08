package com.lxkplus.RandomInit.model;

import lombok.Data;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@ToString
public class DataMatrix {
    LinkedHashMap<String, List<String>> dataColumnList = new LinkedHashMap<>();

    public void addColumn(String field, List<String> dataList) {
        dataColumnList.put(field, dataList);
    }
}


