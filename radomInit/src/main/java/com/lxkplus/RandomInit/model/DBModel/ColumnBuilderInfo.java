package com.lxkplus.RandomInit.model.DBModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("column_info")
public class ColumnBuilderInfo {
    @TableId(type = IdType.AUTO)
    Integer id;
    @TableField("action_id")
    String actionID;
    String userSchemaName;
    String tableName;
    String ColumnName;
    @TableField("builder_ruler")
    String builderRuler;
    String rulerType;
    String params;
    Date insertTime;
    String poolName;
}
