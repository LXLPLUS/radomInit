package com.lxkplus.RandomInit.model;

import lombok.Data;

import java.util.List;

/**
 * 数据和参数类型
 * @param <T>
 */
@Data
public class DataColumn<T> {
    List<T> column;
    String type = null;

    DataColumn (List<T> column) {
        this.column = column;
        if (!column.isEmpty()) {
            type = column.get(0).getClass().getTypeName();
        }
    }
}
