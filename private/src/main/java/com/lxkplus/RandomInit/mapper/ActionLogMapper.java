package com.lxkplus.RandomInit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxkplus.RandomInit.model.ActionLog;

import java.util.List;

public interface ActionLogMapper extends BaseMapper<ActionLog> {
    List<ActionLog> selectMessage(String actionID, Integer step, Integer level);
}
