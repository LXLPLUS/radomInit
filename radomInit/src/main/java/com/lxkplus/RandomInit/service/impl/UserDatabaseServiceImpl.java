package com.lxkplus.RandomInit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxkplus.RandomInit.dto.UserDatabase;
import com.lxkplus.RandomInit.mapper.DatabaseMapper;
import com.lxkplus.RandomInit.mapper.UserDatabaseMapper;
import com.lxkplus.RandomInit.model.entity.Schema;
import com.lxkplus.RandomInit.service.UserDatabaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
* @author Administrator
* @description 针对表【t_user_database】的数据库操作Service实现
* @createDate 2023-10-08 15:00:33
*/
@Service
@Slf4j
public class UserDatabaseServiceImpl extends ServiceImpl<UserDatabaseMapper, UserDatabase>
    implements UserDatabaseService{

    @Resource
    DatabaseMapper databaseMapper;

    @Resource
    UserDatabaseMapper userDatabaseMapper;

    static final String PREFIX = "randominit_temp_";

    @Override
    public List<String> forceDropAllTempSchema() {
        userDatabaseMapper.deleteByMap(Map.of());
        List<Schema> existDatabase = databaseMapper.getExistDatabase();
        for (Schema schema : existDatabase) {
            if (schema != null && schema.getSchemaName() != null) {
                databaseMapper.dropByName(schema.getSchemaName());
            }
        }
        return existDatabase.stream().filter(x -> !Objects.isNull(x)).map(Schema::getSchemaName).toList();
    }

    @Override
    public String createDatabase(String actionID) {
        String databaseName = PREFIX + UUID.randomUUID().toString().substring(0, 6);
        databaseMapper.createDatabase(databaseName);
        // 新增一个记录
        UserDatabase userDatabase = new UserDatabase();
        userDatabase.setActionId(actionID);
        userDatabase.setSchemaName(databaseName);

        userDatabaseMapper.insert(userDatabase);
        log.info("新建数据库: " + databaseName);
        return databaseName;
    }

    @Override
    public List<String> dropDatabase(String actionID) {
        List<UserDatabase> actionIDList = userDatabaseMapper.selectByMap(Map.of("action_id", actionID));
        return dropDatabaseByList(actionID, actionIDList);
    }

    @Override
    public List<String> dropDataBaseByName(String actionID, String databaseName) {
        List<UserDatabase> actionIDList = userDatabaseMapper.selectByMap(Map.of("action_id", actionID, "schema_name", databaseName));
        return dropDatabaseByList(actionID, actionIDList);
    }

    private List<String> dropDatabaseByList(String actionID, List<UserDatabase> actionIDList) {
        for (UserDatabase userDatabase : actionIDList) {
            try {
                databaseMapper.dropByName(userDatabase.getSchemaName());
                log.info("删除{} 下的 {}", userDatabase.getActionId(), userDatabase.getSchemaName());
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("删除数据库{}失败！", userDatabase.getSchemaName());
            }
        }
        userDatabaseMapper.deleteByMap(Map.of("action_id", actionID));
        return actionIDList.stream().map(UserDatabase::getSchemaName).toList();
    }

}




