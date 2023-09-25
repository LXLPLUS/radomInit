package com.lxkplus.RandomInit.enums;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Component
@Slf4j
public class TypeMapper {
    HashMap<String, Class<?>> boxedMap = new HashMap<>();
    HashMap<String, Class<?>> baseMap = new HashMap<>();

    public TypeMapper() {
        baseMap.put("TINYINT", short.class);
        baseMap.put("SMALLINT", int.class);
        baseMap.put("MEDIUMINT", int.class);
        baseMap.put("INT", int.class);
        baseMap.put("BIGINT", long.class);
        baseMap.put("FLOAT", float.class);
        baseMap.put("DOUBLE", double.class);
        baseMap.put("DECIMAL", double.class);
        baseMap.put("BOOLEAN", boolean.class);
        baseMap.put("BOOL", boolean.class);

        baseMap.put("DATE", Date.class);
        baseMap.put("TIME", Date.class);
        baseMap.put("YEAR", Date.class);
        baseMap.put("DATETIME", Date.class);
        baseMap.put("TIMESTAMP", Date.class);

        boxedMap.put("TINYINT", Short.class);
        boxedMap.put("SMALLINT", Integer.class);
        boxedMap.put("MEDIUMINT", Integer.class);
        boxedMap.put("INT", Integer.class);
        boxedMap.put("BIGINT", Long.class);
        boxedMap.put("FLOAT", Float.class);
        boxedMap.put("DOUBLE", Double.class);
        boxedMap.put("DECIMAL", BigDecimal.class);
        boxedMap.put("BOOLEAN", Boolean.class);
        boxedMap.put("BOOL", Boolean.class);

        boxedMap.put("DATE", Date.class);
        boxedMap.put("TIME", Date.class);
        boxedMap.put("YEAR", Date.class);
        boxedMap.put("DATETIME", Date.class);
        boxedMap.put("TIMESTAMP", Date.class);
    }

    public Class<?> get(String typeName, boolean boxed, boolean allString) {
        if (allString) {
            return String.class;
        }
        if (typeName != null) {
            typeName = typeName.toUpperCase();
        }
        if (boxed) {
            return boxedMap.getOrDefault(typeName, String.class);
        }
        return baseMap.getOrDefault(typeName, String.class);
    }
}
