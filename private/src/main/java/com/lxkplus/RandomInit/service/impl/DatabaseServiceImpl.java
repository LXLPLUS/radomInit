package com.lxkplus.RandomInit.service.impl;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.StatusEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.DatabaseMapper;
import com.lxkplus.RandomInit.service.DatabaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {

    static final String prefix = "randomInit_temp_".toLowerCase();
    static final String defaultName = "default";

    @Resource
    DatabaseMapper databaseMapper;

    @Override
    public String getRealDatabaseName(String actionID, String name) {
        return prefix + actionID + "_" + name;
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

    private List<String> getAllDatabase() {
        List<String> existDatabase = databaseMapper.getExistDatabase();
        return existDatabase.stream().filter(x -> x.startsWith(prefix)).collect(Collectors.toList());
    }

    @Override
    public void checkID(String actionID) throws NormalErrorException {
        ThrowUtils.throwIfNullOrBlack(actionID, "actionID不能为空");
        ThrowUtils.throwIf(!actionID.matches("[A-Za-z0-9]+"), ErrorEnum.paramNotSupport,"actionID只支持英文字母和数字");
        ThrowUtils.throwIf(actionID.length() > 10, ErrorEnum.paramNotSupport, "id过长超过最大长度10！");
    }

    @Override
    public void checkIDAndName(String actionID, String databaseName) throws NormalErrorException {
        checkID(actionID);
        ThrowUtils.throwIfNullOrBlack(databaseName, "数据库名不能为空");
        ThrowUtils.throwIf(!actionID.matches("[A-Za-z0-9_]+"), ErrorEnum.paramNotSupport,"数据库名只支持英文字母和数字、下划线");
        ThrowUtils.throwIf(databaseName.length() > 30, ErrorEnum.paramNotSupport, "数据库名过长！");
    }

    private List<String> getRealDatabaseByID(String actionId) throws NormalErrorException {
        checkID(actionId);
        return getAllDatabase().stream()
                .filter(x -> x.startsWith(prefix + actionId + "_"))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDatabaseByID(String actionID) throws NormalErrorException {
        checkID(actionID);
        List<String> databaseNameList = getAllDatabase().stream()
                .filter(x -> x.startsWith(prefix + actionID + "_"))
                .collect(Collectors.toList());

        List<String> namespaceList = new ArrayList<>();
        for (String databaseName : databaseNameList) {
            namespaceList.add(getFromRealDatabaseName(databaseName).getRight());
        }
        return namespaceList;

    }


    @Override
    public boolean checkDatabaseExist(String actionID, String databaseName) throws NormalErrorException {
        checkIDAndName(actionID, databaseName);
        return getAllDatabase().contains(getRealDatabaseName(actionID, databaseName));
    }

    @Override
    public String createDatabase(String actionID, String databaseID) throws NormalErrorException {
        checkIDAndName(actionID, databaseID);
        String nameWithPrefix = getRealDatabaseName(actionID, databaseID);

        // 只获取对应前缀的数据库进行操作，防止误伤
        // 对应前缀的数据库必然是一次性的数据库
        Set<String> existDatabase = databaseMapper.getExistDatabase().stream()
                .filter(x -> x.startsWith(prefix))
                .collect(Collectors.toSet());

        ThrowUtils.throwIf(existDatabase.contains(nameWithPrefix), ErrorEnum.Exist, String.format("对应数据库%s存在", nameWithPrefix));

        databaseMapper.createDatabase(nameWithPrefix);
        log.info("成功新建数据库{}", nameWithPrefix);
        return prefix;
    }

    @Override
    public String createDatabase(String actionId) throws NormalErrorException {
        checkID(actionId);
        return createDatabase(actionId, defaultName);
    }

    @Override
    public List<Map<String, String>> dropDatabaseByID(String actionID) throws NormalErrorException {
        checkID(actionID);
        List<String> initDatabases = getRealDatabaseByID(actionID);
        List<Map<String, String>> deleteAns = new ArrayList<>();
        log.info("删除{}下的所有数据库", actionID);
        log.info("被删除的数据库有{}", StringUtils.join(initDatabases, ","));
        for (String databaseName : initDatabases) {
            deleteAction(deleteAns, databaseName);
        }
        log.info("删除结果： " + deleteAns);
        return deleteAns;
    }

    @Override
    public List<Map<String, String>> dropAllDatabase() {
        List<String> initDatabases = getAllDatabase();
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
    public List<Map<String, String>> dropDatabaseByName(String actionID, String name) throws NormalErrorException {
        checkIDAndName(actionID, name);
        List<Map<String, String>> deleteAns = new ArrayList<>();
        String databaseName = getRealDatabaseName(actionID, name);
        if (!checkDatabaseExist(actionID, name)) {
            deleteAns.add(Map.of("databaseName", databaseName, "status", StatusEnum.NOT_EXIST.name()));
        }
        deleteAction(deleteAns, databaseName);
        log.info("删除结果： " + deleteAns);
        return deleteAns;
    }

}
