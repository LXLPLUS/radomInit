package com.lxkplus.RandomInit.enums;

import lombok.Getter;

@Getter
public enum MysqlEnum {
    FLOAT("FLOAT"),
    DECIMAL("DECIMAL"),
    VARCHAR("VARCHAR"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    INT("INT"),
    TEXT("TEXT"),
    JSON("JSON"),
    DOUBLE("DOUBLE"),
    BOOL("BOOL"),
    LONG("LONG"),
    TINYINT("TINYINT"),
    BIGINT("BIGINT"),
    TIMESTAMP("TIMESTAMP"),

    TYPE("TYPE"),
    FIELD("FIELD"),

    CURRENT_TIMESTAMP("CURRENT_TIMESTAMP"),
    CURRENT_TIMESTAMP_ON_UPDATE("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"),

    COMMIT("COMMIT"),
    COMMENT("COMMENT"),
    DEFAULT("DEFAULT"),
    NOT_NULL("NOT NULL"),

    NULL("NULL"),

    KEY("KEY"),
    INDEX("KEY"),
    UNIQUE_KEY("UNIQUE KEY"),
    PRIMARY_KEY("PRIMARY KEY"),
    FULLTEXT("FULLTEXT"),

    AUTO_INCREMENT("AUTO_INCREMENT"),

    DefaultEngine("InnoDB"),
    DefaultCharSet("utf8mb4");


    String realParam;

    MysqlEnum(String realParam) {
        this.realParam = realParam;
    }
}
