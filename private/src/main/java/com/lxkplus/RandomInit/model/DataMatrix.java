package com.lxkplus.RandomInit.model;

import lombok.Data;
import lombok.ToString;

import java.util.*;

@Data
@ToString
public class DataMatrix {
    LinkedHashMap<String, DataColumn<?>> dataColumnList = new LinkedHashMap<>();

    public void addColumn(String name, List<?> dataList) {
        dataColumnList.put(name, new DataColumn<>(dataList));
    }

    /**
     * 转变为excel对应的格式
     * @return 列表
     */
    public List<List<Object>> toMatrix() {
        ArrayList<List<Object>> objects = new ArrayList<>();
        objects.add(new ArrayList<>(dataColumnList.keySet()));

        // 列转行,用有序map实现
        TreeMap<Integer, List<Object>> sortedMap = new TreeMap<>();
        for (DataColumn<?> value : dataColumnList.values()) {
            List<?> column = value.column;
            for (int i = 0; i < column.size(); i++) {
                sortedMap.computeIfAbsent(i, k -> new ArrayList<>());
                sortedMap.get(i).add(column.get(i));
            }
        }
        objects.addAll(sortedMap.values());
        return objects;
    }
}
