package com.lxkplus.RandomInit.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TableMessage {
    String tableName;
    Integer count;
    Date updateTime;
}
