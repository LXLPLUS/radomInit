package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.dto.CreateSql;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxkplus.RandomInit.dto.TableMessage;
import com.lxkplus.RandomInit.dto.TestDDL;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_create_sql(建表语句记录)】的数据库操作Service
* @createDate 2023-10-08 19:16:30
*/
public interface CreateSqlService extends IService<CreateSql> {

    TestDDL testAndSaveDDL(String sql);

    List<TableMessage> getDataByActionID();
}
