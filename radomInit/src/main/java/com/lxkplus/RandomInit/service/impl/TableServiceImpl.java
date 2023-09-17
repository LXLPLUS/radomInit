package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.google.common.base.CaseFormat;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.enums.MysqlEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.exception.ThrowUtils;
import com.lxkplus.RandomInit.mapper.TableActionMapper;
import com.lxkplus.RandomInit.model.VO.TableParams;
import com.lxkplus.RandomInit.service.MysqlCheckService;
import com.lxkplus.RandomInit.service.SchemaService;
import com.lxkplus.RandomInit.service.StatementService;
import com.lxkplus.RandomInit.service.TableService;
import com.lxkplus.RandomInit.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class TableServiceImpl implements TableService {

    @Resource
    SchemaService schemaService;

    @Resource
    StatementService sqlService;

    @Resource
    JsonUtils jsonUtils;

    @Resource
    TableActionMapper tableMapper;

    @Resource
    MysqlCheckService mysqlCheckService;

    @Override
    public MySqlCreateTableStatement convertVoToStatement(TableParams tableParams) {

        MySqlCreateTableStatement statement = new MySqlCreateTableStatement();

        String tableName = tableParams.getTableHeader().getTableName();

        tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableName);

        if (StringUtils.isBlank(tableName)) {
            tableName = jsonUtils.faker.animal().name();
        }
        statement.setName(tableName);

        String engine = tableParams.getTableHeader().getEngine();
        if (StringUtils.isNotBlank(engine)) {
            statement.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("ENGINE"), new SQLIdentifierExpr(engine)));
        }

        String charSet = tableParams.getTableHeader().getCharSet();
        if (StringUtils.isNotBlank(charSet)) {
            statement.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("CHARSET"), new SQLIdentifierExpr(charSet)));
        }

        String sortRuler = tableParams.getTableHeader().getSortRuler();
        if (StringUtils.isNotBlank(sortRuler)) {
            statement.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("COLLATE"), new SQLIdentifierExpr(sortRuler)));
        }

        String comment = tableParams.getTableHeader().getComment();
        if (StringUtils.isNotBlank(comment)) {
            statement.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("COMMENT"), new SQLIdentifierExpr(sortRuler)));
        }

        statement.setIfNotExiists(tableParams.getTableHeader().isIfNotExist());

        // 填入行信息
        for (TableParams.TableColumn tableColumn : tableParams.getTableColumns()) {
            SQLColumnDefinition sqlColumnDefinition = new SQLColumnDefinition();

            String rowName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableColumn.getRowName());
            sqlColumnDefinition.setName(rowName);

            if (StringUtils.isNotBlank(tableColumn.getComment())) {
                sqlColumnDefinition.setComment(tableColumn.getComment());
            }

            if (Objects.equals(tableColumn.getExtra(), MysqlEnum.AUTO_INCREMENT.getRealParam())) {
                sqlColumnDefinition.setAutoIncrement(true);
            }

            if (Objects.equals(tableColumn.getExtra(), MysqlEnum.CURRENT_TIMESTAMP_ON_UPDATE.getRealParam())) {
                sqlColumnDefinition.setOnUpdate(new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_TIMESTAMP));
            }

            if (!tableColumn.isAllowNull()) {
                sqlColumnDefinition.addConstraint(new SQLNotNullConstraint());
            }

            String dataType = tableColumn.getDataType();
            sqlColumnDefinition.setDataType(new SQLCharacterDataType(dataType));

            if (Objects.equals(tableColumn.getDefault(), MysqlEnum.CURRENT_TIMESTAMP.getRealParam())) {
                sqlColumnDefinition.setDefaultExpr(new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_TIMESTAMP));
            } else if (StringUtils.isNotBlank(tableColumn.getDefault())) {
                sqlColumnDefinition.setDefaultExpr(new SQLCharExpr(tableColumn.getDefault()));
            }

            sqlColumnDefinition.setComment(tableColumn.getComment());

            statement.addColumn(sqlColumnDefinition);
        }

        for (TableParams.TableIndex tableIndex : tableParams.getTableIndices()) {
            MySqlKey mySqlKey;
            if (tableIndex.getIndexType().equals(MysqlEnum.PRIMARY_KEY.getRealParam())) {
                mySqlKey = new MySqlPrimaryKey();
            } else {
                mySqlKey = new MySqlKey();
            }

            if (StringUtils.isNotBlank(tableIndex.getIndexName())) {
                mySqlKey.setName(tableIndex.getIndexName());
            }

            for (String indexColumn : tableIndex.getIndexColumns()) {
                SQLSelectOrderByItem sqlSelectOrderByItem = new SQLSelectOrderByItem();
                sqlSelectOrderByItem.setExpr(new SQLIdentifierExpr(indexColumn));
                mySqlKey.addColumn(sqlSelectOrderByItem);
            }

            statement.getTableElementList().add(mySqlKey);

        }
        return statement;
    }

    @Override
    public void createTable(String actionID, String userSchemaName, String sql) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        mysqlCheckService.checkDataBaseNameSafe(userSchemaName);

        String realSchemaName = schemaService.getRealSchemaName(actionID, userSchemaName);
        ThrowUtils.throwIf(!mysqlCheckService.checkDatabaseExist(actionID, userSchemaName, false),
                ErrorEnum.Empty,
                realSchemaName + "数据库不存在");

        MySqlCreateTableStatement statement = sqlService.readCreateSql(sql);
        String tableName = StringUtils.strip(statement.getTableName(), "`");

        // 如果数据库存在那么跳过
        ThrowUtils.throwIf(mysqlCheckService.checkTableExist(actionID, userSchemaName, tableName, false),
                ErrorEnum.Exist,
                String.format("action:%s 数据库:%s对应表%s存在", actionID, userSchemaName, tableName));
        statement.setIfNotExiists(false);
        statement.setSchema(realSchemaName);

        tableMapper.createTable(statement.toString());
    }

    @Override
    public int clearData(String actionID, String userSchemaName, String tableName) throws NormalErrorException {
        // 检查参数是否合法和是否存在
        mysqlCheckService.checkTableNameSafe(actionID, userSchemaName, tableName);

        ThrowUtils.throwIf(!mysqlCheckService.checkTableExist(actionID, userSchemaName, tableName, false),
                ErrorEnum.Empty,
                String.format("操作的表格%s不存在！", tableName));

        String realSchemaName = schemaService.getRealSchemaName(actionID, userSchemaName);

        int count = tableMapper.countRows(realSchemaName, tableName);
        tableMapper.clearData(realSchemaName, tableName);
        return count;
    }

    @Override
    public String getTableDDL(String actionID, String databaseName, String tableName) throws NormalErrorException {
        // DDL里面没有数据库信息
        mysqlCheckService.checkTableNameSafe(actionID, databaseName, tableName);

        ThrowUtils.throwIf(!mysqlCheckService.checkTableExist(actionID, databaseName, tableName, false),
                ErrorEnum.Empty,
                String.format("操作的表格%s不存在！", tableName));

        String realSchemaName = schemaService.getRealSchemaName(actionID, databaseName);

        Map<String, String> params = tableMapper.getCreateDDL(realSchemaName, tableName);
        return params.getOrDefault("Create Table", null);
    }

}
