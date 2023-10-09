package com.lxkplus.RandomInit.service;

import com.lxkplus.RandomInit.dto.UserDatabase;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_user_database】的数据库操作Service
* @createDate 2023-10-08 15:00:33
*/
public interface UserDatabaseService extends IService<UserDatabase> {

    List<String> forceDropAllTempSchema();

    String createDatabase(String actionID);

    List<String> dropDatabase(String actionID);

    List<String> dropDataBaseByName(String actionID, String databaseName);
}
