package com.lxkplus.RandomInit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lxkplus.RandomInit.enums.MysqlEnum;
import com.lxkplus.RandomInit.utils.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class MysqlColumn {

    @JsonIgnore
    @Resource
    JsonUtils jsonUtils;

    @NotNull
    String columnName;
    String type = MysqlEnum.VARCHAR.getRealParam();
    List<String> typeArgument = List.of("255");
    boolean notNull = false;
    String defaultMessage = "";
    String comment = "";
    boolean autoIncrease = false;
    boolean insertTime = false;
    boolean updateTime = false;
    RandomParam buildRuler = null;

    public void setUpdateTime(boolean updateTime) {
        this.type = MysqlEnum.DATETIME.getRealParam();
        this.defaultMessage = "";
        this.updateTime = updateTime;
    }

    public void setInsertTime(boolean insertTime) {
        this.type = MysqlEnum.DATETIME.getRealParam();
        this.defaultMessage = "";
        this.insertTime = insertTime;
    }

    public String getColumnName() {
        if (StringUtils.isBlank(columnName)) {
            columnName = jsonUtils.faker.pokemon().name();
        }
        return "`" + columnName + "`";
    }

    public void setColumnName(String columnName) {
        this.columnName = StringUtils.strip(columnName, "`");
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = StringUtils.strip(defaultMessage, "'`");
    }
}
