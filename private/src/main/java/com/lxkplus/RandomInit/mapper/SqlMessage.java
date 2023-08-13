package com.lxkplus.RandomInit.mapper;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sql_active")
public class SqlMessage {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    @TableField(value = "sql_name")
    String sql;
    @TableField(value = "database_name")
    String tableName;
    @TableField(value = "user_id")
    int userID;
    String comment;
}
