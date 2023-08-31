package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.StatusEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.ColumnBuilderInfoMapper;
import com.lxkplus.RandomInit.mapper.DatabaseMapper;
import com.lxkplus.RandomInit.service.SchemaService;
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
public class SchemaServiceImpl implements SchemaService {

    static final String prefix = "randomInit_temp_".toLowerCase();
    static final String defaultDatabaseName = "default";

    @Resource
    MysqlCheckService mysqlCheckService;

    @Resource
    DatabaseMapper databaseMapper;

    @Resource
    ColumnBuilderInfoMapper columnBuilderInfoMapper;

    @Override
    public String getRealSchemaName(String actionID, String userSchemaName) {
        return prefix + actionID + "_" + userSchemaName;
    }

    @Override
    public Pair<String, String> getFromRealSchemaName(String realSchemaName) throws NormalErrorException {
        ThrowUtils.throwIf(!realSchemaName.startsWith(prefix),
                ErrorEnum.paramNotSupport,
                "对这个数据库操作是危险的！不是" + prefix);
        realSchemaName = realSchemaName.replaceFirst(prefix, "");
        String[] dataList = realSchemaName.split("_", 2);

        ThrowUtils.throwIf(dataList.length < 2, ErrorEnum.NotEnoughParams, realSchemaName + "不是有效的数据库名！");
        return Pair.of(dataList[0], dataList[1]);
    }

    private List<String> getAllSchemaRealName() {
        List<String> existDatabase = databaseMapper.getExistDatabase();
        return existDatabase.stream().filter(x -> x.startsWith(prefix)).collect(Collectors.toList());
    }

    private List<String> getSchemaNameByID(String actionId) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionId);
        return getAllSchemaRealName().stream()
                .filter(x -> x.startsWith(prefix + actionId + "_"))
                .collect(Collectors.toList());
    }

    @Override
    public String createSchema(String actionID, String userSchemaName) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        mysqlCheckService.checkDataBaseNameSafe(userSchemaName);
        String nameWithPrefix = getRealSchemaName(actionID, userSchemaName);

        ThrowUtils.throwIf(mysqlCheckService.checkDatabaseExist(actionID, userSchemaName, false),
                ErrorEnum.Exist,
                String.format("对应数据库%s存在", nameWithPrefix));

        databaseMapper.createDatabase(nameWithPrefix);
        log.info("成功新建数据库{}", nameWithPrefix);
        return prefix;
    }

    @Override
    public String createSchema(String actionID) throws NormalErrorException {
        mysqlCheckService.checkTableNameSafe(actionID);
        return createSchema(actionID, defaultDatabaseName);
    }

    @Override
    public List<Map<String, String>> dropSchemaByID(String actionID) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        List<String> initDatabases = getSchemaNameByID(actionID);
        List<Map<String, String>> deleteAns = new ArrayList<>();
        log.info("删除{}下的所有数据库", actionID);
        log.info("被删除的数据库有{}", StringUtils.join(initDatabases, ","));
        for (String databaseName : initDatabases) {
            deleteAction(deleteAns, databaseName);
        }

        // 清空写入的规则
        columnBuilderInfoMapper.deleteByMap(Map.of("action_id", actionID));
        log.info("删除结果： " + deleteAns);

        return deleteAns;
    }

    @Override
    public List<Map<String, String>> dropAllSchema() {
        List<String> initDatabases = getAllSchemaRealName();
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
    public List<Map<String, String>> dropSchemaByName(String actionID, String userSchemaName) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        mysqlCheckService.checkDataBaseNameSafe(userSchemaName);
        List<Map<String, String>> deleteAns = new ArrayList<>();
        String databaseName = getRealSchemaName(actionID, userSchemaName);
        if (!mysqlCheckService.checkDatabaseExist(actionID, userSchemaName, false)) {
            deleteAns.add(Map.of("databaseName", databaseName, "status", StatusEnum.NOT_EXIST.name()));
        }
        deleteAction(deleteAns, databaseName);
        log.info("删除结果： " + deleteAns);
        return deleteAns;
    }

}
