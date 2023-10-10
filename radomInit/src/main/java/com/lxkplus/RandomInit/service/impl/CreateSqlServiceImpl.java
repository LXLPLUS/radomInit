package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxkplus.RandomInit.dto.CreateSql;
import com.lxkplus.RandomInit.dto.TableMessage;
import com.lxkplus.RandomInit.dto.TestDDL;
import com.lxkplus.RandomInit.mapper.CreateSqlMapper;
import com.lxkplus.RandomInit.mapper.TableActionMapper;
import com.lxkplus.RandomInit.service.CreateSqlService;
import com.lxkplus.RandomInit.service.UserDatabaseService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_create_sql(建表语句记录)】的数据库操作Service实现
* @createDate 2023-10-08 19:16:30
*/
@Service
public class CreateSqlServiceImpl extends ServiceImpl<CreateSqlMapper, CreateSql>
    implements CreateSqlService{

    @Resource
    UserDatabaseService userDatabaseService;

    @Resource
    ActionIdServiceImpl actionIdService;

    @Resource
    TableActionMapper tableActionMapper;

    @Resource
    CreateSqlMapper createSqlMapper;

    @Override
    public TestDDL testThenSaveDDL(String sql) {

        TestDDL testDDL = new TestDDL();
        if (StringUtils.isBlank(sql)) {
            testDDL.setMessage("字符串为空");
        }

        try {
            SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
            if (sqlStatement instanceof MySqlCreateTableStatement statement) {
                String actionID = actionIdService.getActionID().toString();
                String databaseName = userDatabaseService.createDatabase(actionID);
                statement.setSchema(databaseName);
                // 不记录任何数据，直接操作以后删除
                tableActionMapper.createTable(statement.toString());
                statement.setSchema(null);
                userDatabaseService.dropDataBaseByName(actionID, databaseName);

                // 成功入库的代码记录到数据库中
                CreateSql createSql = new CreateSql();
                createSql.setActionId(actionID);
                createSql.setCreateSql(statement.toString());
                createSql.setTableName(StringUtils.strip(statement.getTableName(), "`"));
                createSql.setColumnsCount(statement.getColumnDefinitions().size());
                createSql.setIndexCount(statement.getMysqlKeys().size());
                if (statement.getComment() != null) {
                    createSql.setComment(StringUtils.strip(statement.getComment().toString(), "'"));
                }
                createSqlMapper.insert(createSql);

                // 一切都完成以后返回状态为成功
                testDDL.setPass(true);
            }
            else {
                testDDL.setMessage("不是建表语句");
            }
        } catch (Exception e) {
            e.printStackTrace();
            testDDL.setMessage("解析sql失败！" + e.getMessage());
        }
        return testDDL;
    }

    @Override
    public List<TableMessage> getDataByActionID() {
        Integer actionID = actionIdService.getActionID();
        return createSqlMapper.getAllByActionIdAndIsDelete(actionID.toString());
    }
}




