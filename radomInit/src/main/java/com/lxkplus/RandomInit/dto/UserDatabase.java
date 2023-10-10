package com.lxkplus.RandomInit.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_user_database
 */
@TableName(value ="t_user_database")
@Data
public class UserDatabase implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 操作ID
     */
    private String actionId;

    /**
     * 数据库名称
     */
    private String schemaName;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 
     */
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}