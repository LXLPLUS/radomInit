package com.lxkplus.RandomInit.template;

import com.lxkplus.RandomInit.enums.MysqlEnum;
import com.lxkplus.RandomInit.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
class MysqlRow {
    String rowName;
    String typeName;
    Integer len;
    Boolean allowNULL;
    Object defaultMessage;
    String commit;
    boolean primaryKey;
}

@Data
public class MysqlCreate {
    String tableName;
    List<MysqlRow> mysqlRows = new ArrayList<>();
    String engineName;
    String charSet;
    static HashMap<String, MysqlRow> defaultMaker = new HashMap<>();

    public MysqlCreate(String tableName, String engineName, String charSet) {
        initDefault();
        this.tableName = tableName;
        this.engineName = engineName;
        this.charSet = charSet;
    }

    public MysqlCreate(String tableName) {
        initDefault();
        this.tableName = tableName;
        this.engineName = "InnoDB";
        this.charSet = "utf8mb4";

    }


    public void addRow(String rowName, String typeName, Integer len, boolean isNULL, Object defaultMessage,
                       String commit, boolean primaryKey) {
        mysqlRows.add(new MysqlRow(rowName, typeName, len, isNULL, defaultMessage, commit, primaryKey));
    }

    public void addRow(String rowName, String typeName){
        MysqlRow mysqlRow = new MysqlRow();
        if (defaultMaker.containsKey(typeName)) {
            BeanUtils.copyProperties(defaultMaker.get(typeName), mysqlRow);
            mysqlRow.setRowName(rowName);
        }
        else {
            mysqlRow = new MysqlRow(rowName, typeName, null, true, null, "", false);
        }
        mysqlRows.add(mysqlRow);

    }

    public Map<?, ?> showData() {
        return new MysqlShowData(this).toMap();
    }

    void initDefault() {
        /*
          生成默认参数
         */
        if (defaultMaker.isEmpty()) {
            defaultMaker.put(String.class.getTypeName(), new MysqlRow(
                    "", MysqlEnum.VARCHAR.name(), 255, false, "", "", false));
            defaultMaker.put(Integer.class.getTypeName(),
                    new MysqlRow("", MysqlEnum.INT.name(), 10, false, 0, "", false));
            defaultMaker.put(Float.class.getTypeName(), new MysqlRow(
                    "", MysqlEnum.FLOAT.name(), 10, false, 0, "", false));
            defaultMaker.put(Date.class.getTypeName(), new MysqlRow(
                    "", MysqlEnum.DATETIME.name(),null, false, MysqlEnum.CURRENT_TIMESTAMP, "", false));
            defaultMaker.put(BigDecimal.class.toString(), new MysqlRow(
                    "", MysqlEnum.DECIMAL.name() + "(19, 4)", null, false, 0, "", false));
        }
    }
}

/**
 * 格式化的工具类，将提供的数据转化为mysql对应的Map，以便写入模板
 */
@Data
class MysqlShowData {
    String tableName;
    List<String> mysqlRows = new ArrayList<>();
    String engineName;
    String charSet;

    MysqlShowData(MysqlCreate mysqlInitData) {
        this.tableName = StringUtils.strip(mysqlInitData.tableName, "`");
        this.engineName = mysqlInitData.engineName;
        this.charSet = mysqlInitData.charSet;

        for (MysqlRow mysqlRow : mysqlInitData.mysqlRows) {
            String rowName = StringUtils.strip(mysqlRow.getRowName(), "`");
            String typeName = mysqlRow.getTypeName();
            String len;
            String isNULL = BooleanUtils.toString(mysqlRow.allowNULL, "", "NOT NULL");
            String primaryKey = BooleanUtils.toString(mysqlRow.isPrimaryKey(), "PRIMARY KEY", "");
            String defaultMessage;
            String commit;
            if (mysqlRow.getLen() == null) {
                len = "";
            }
            else {
                len = String.format("(%d)", mysqlRow.getLen());
            }
            if (mysqlRow.getDefaultMessage() == null) {
                defaultMessage = "DEFAULT NULL";
            }
            else if (mysqlRow.getDefaultMessage() instanceof MysqlEnum) {
                defaultMessage = "DEFAULT " + ((MysqlEnum) mysqlRow.getDefaultMessage()).name();
            }
            else {
                defaultMessage = "DEFAULT '" + mysqlRow.getDefaultMessage().toString() + "'";
            }
            if (mysqlRow.getCommit() == null) {
                commit = "";
            }
            else {
                commit = "COMMIT '" + mysqlRow.getCommit() + "'";
            }

            String format = String.format("`%s` %s%s %s %s %s %s",
                    rowName, typeName, len, isNULL, primaryKey, defaultMessage, commit);
            mysqlRows.add(format);
        }
    }

    Map<?, ?> toMap() {
        return JsonUtils.objectMapper.convertValue(this, Map.class);
    }
}