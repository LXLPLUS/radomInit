package com.lxkplus.RandomInit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_pool_register")
public class PoolRegister {
    @TableId(type = IdType.AUTO)
    Integer id;
    @TableField("action_id")
    String actionID;
    String rulerType;
    String poolName;
    String params;
    Date insertTime;
}
