package com.lxkplus.RandomInit.model.DBModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_login")
public class UserLogin {
    @TableId(type = IdType.AUTO)
    Integer id;
    String userName;
    String password;
    Date createTime;
    int level;
    @TableField("action_id")
    String actionID;
    String comment;
}
