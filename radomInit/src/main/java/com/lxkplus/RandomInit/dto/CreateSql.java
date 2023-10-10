package com.lxkplus.RandomInit.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_create_sql")
public class CreateSql {
    /**
     * id
     */
    @TableId(
            type = IdType.AUTO
    )
    @NotNull
    private Integer id;

    @NotNull
    private Date createTime;

    @NotNull
    private Date updateTime;

    /**
     * 操作ID
     */
    @NotNull
    private String actionId;

    /**
     * 建表语句
     */
    @NotNull
    private String createSql;

    @NotNull
    private String tableName;

    /**
     * 注释
     */
    private String comment;

    /**
     * 行数
     */
    private Integer columnsCount;

    /**
     * 索引数
     */
    private Integer indexCount;

    @NotNull
    private Short isDelete;
}
