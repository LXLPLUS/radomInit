package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.StatusEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.ColumnInfoMapper;
import com.lxkplus.RandomInit.mapper.DatabaseMapper;
import com.lxkplus.RandomInit.service.DatabaseService;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {

    static final String prefix = "randomInit_temp_".toLowerCase();
    static final String defaultName = "default";

    @Resource
    MysqlCheckService mysqlCheckService;

    @Resource
    DatabaseMapper databaseMapper;

    @Resource
    ColumnInfoMapper columnInfoMapper;

    @Override
    public String getRealDatabaseName(String actionID, String userDatabaseName) {
        return prefix + actionID + "_" + userDatabaseName;
    }

    @Override
    public Pair<String, String> getFromRealDatabaseName(String databaseName) throws NormalErrorException {
        ThrowUtils.throwIf(!databaseName.startsWith(prefix),
                ErrorEnum.paramNotSupport,
                "对这个数据库操作是危险的！不是" + prefix);
        databaseName = databaseName.replaceFirst(prefix, "");
        String[] dataList = databaseName.split("_", 2);

        ThrowUtils.throwIf(dataList.length < 2, ErrorEnum.NotEnoughParams, databaseName + "不是有效的数据库名！");
        return Pair.of(dataList[0], dataList[1]);
    }

    private List<String> getAllDatabaseRealName() {
        List<String> existDatabase = databaseMapper.getExistDatabase();
        return existDatabase.stream().filter(x -> x.startsWith(prefix)).collect(Collectors.toList());
    }

    private List<String> getRealDatabaseByID(String actionId) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionId);
        return getAllDatabaseRealName().stream()
                .filter(x -> x.startsWith(prefix + actionId + "_"))
                .collect(Collectors.toList());
    }

    @Override
    public String createDatabase(String actionID, String userDatabaseName) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        mysqlCheckService.checkDataBaseNameSafe(userDatabaseName);
        String nameWithPrefix = getRealDatabaseName(actionID, userDatabaseName);

        ThrowUtils.throwIf(mysqlCheckService.checkDatabaseExist(actionID, userDatabaseName, false),
                ErrorEnum.Exist,
                String.format("对应数据库%s存在", nameWithPrefix));

        databaseMapper.createDatabase(nameWithPrefix);
        log.info("成功新建数据库{}", nameWithPrefix);
        return prefix;
    }

    @Override
    public String createDatabase(String actionID) throws NormalErrorException {
        mysqlCheckService.checkTableNameSafe(actionID);
        return createDatabase(actionID, defaultName);
    }

    @Override
    public List<Map<String, String>> dropDatabaseByID(String actionID) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        List<String> initDatabases = getRealDatabaseByID(actionID);
        List<Map<String, String>> deleteAns = new ArrayList<>();
        log.info("删除{}下的所有数据库", actionID);
        log.info("被删除的数据库有{}", StringUtils.join(initDatabases, ","));
        for (String databaseName : initDatabases) {
            deleteAction(deleteAns, databaseName);
        }

        // 清空写入的规则
        columnInfoMapper.deleteByMap(Map.of("action_id", actionID));
        log.info("删除结果： " + deleteAns);

        return deleteAns;
    }

    @Override
    public List<Map<String, String>> dropAllDatabase() {
        List<String> initDatabases = getAllDatabaseRealName();
        List<Map<String, String>> deleteAns = new ArrayList<>();
        log.info("删除所有数据库");
        log.info("被删除的数据库有{}", StringUtils.join(initDatabases, ","));
        for (String databaseName : initDatabases) {
            deleteAction(deleteAns, databaseName);
        }
        log.info("删除结果： " + deleteAns);

        return deleteAns;
    }

    private void deleteAction(List<Map<String, String>> deleteAns, String databaseName) {
        try {
            databaseMapper.dropByName(databaseName);
            deleteAns.add(Map.of("databaseName", databaseName, "status", StatusEnum.SUCCESS.name()));
        }
        catch (Exception e) {
            deleteAns.add(Map.of("databaseName", databaseName, "status", StatusEnum.FAILED.name()));
            log.warn("{} 删除时报错{}", databaseName, e.getCause().toString());
        }
    }

    @Override
    public List<Map<String, String>> dropDatabaseByName(String actionID, String userDatabaseName) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        mysqlCheckService.checkDataBaseNameSafe(userDatabaseName);
        List<Map<String, String>> deleteAns = new ArrayList<>();
        String databaseName = getRealDatabaseName(actionID, userDatabaseName);
        if (!mysqlCheckService.checkDatabaseExist(actionID, userDatabaseName, false)) {
            deleteAns.add(Map.of("databaseName", databaseName, "status", StatusEnum.NOT_EXIST.name()));
        }
        deleteAction(deleteAns, databaseName);
        log.info("删除结果： " + deleteAns);
        return deleteAns;
    }

}
