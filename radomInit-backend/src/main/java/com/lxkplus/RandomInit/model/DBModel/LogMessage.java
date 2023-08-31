package com.lxkplus.RandomInit.model.DBModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("action_log")
public class LogMessage {
    @TableId(type = IdType.AUTO)
    Integer id;
    @TableField("action_id")
    String actionID;
    Integer step;
    String message;
    String level;
    Date insertTime;

}
