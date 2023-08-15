package com.lxkplus.RandomInit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class DataCell {
    String actionID;
    String database;
    String tableName;
    String value;
}
