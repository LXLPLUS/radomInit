package com.lxkplus.RandomInit.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@TableName("builder_ruler")
public class BuilderRuler {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id = 0;
    @TableField("action_id")
    String actionID;
    String databaseName;
    String tableName;
    String ruler;
    String value;

    public BuilderRuler(String actionID, String databaseName, String table_name, String ruler, String value) {
        this.actionID = actionID;
        this.databaseName = databaseName;
        this.tableName = table_name;
        this.ruler = ruler;
        this.value = value;
    }
}
