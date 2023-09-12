package com.lxkplus.RandomInit.model.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_ruler_register")
public class RegexRegisterDo {
    @TableId(type = IdType.AUTO)
    int id;
    @TableField("action_id")
    String actionID;
    String rulerType;
    /*
    理论上来说，是应该多表join的，但是我定的是规则不可改变
    既然规则不可改变，那么就没有新增的必要
     */
    String Params;
    String example;
    Date insertTime;
}
