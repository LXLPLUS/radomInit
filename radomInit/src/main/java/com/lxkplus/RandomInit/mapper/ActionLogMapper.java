package com.lxkplus.RandomInit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxkplus.RandomInit.model.entity.LogMessage;

import java.util.List;

public interface ActionLogMapper extends BaseMapper<LogMessage> {
    List<LogMessage> selectMessage(String actionID, Integer step, String level);
}
