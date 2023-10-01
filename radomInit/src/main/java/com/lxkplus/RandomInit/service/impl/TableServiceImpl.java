package com.lxkplus.RandomInit.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.github.javafaker.Faker;
import com.lxkplus.RandomInit.commons.StringOptional;
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
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class TableServiceImpl implements TableService {

    @Resource
    SchemaService schemaService;

    @Resource
    StatementService sqlService;

    @Resource
    Faker faker;

    @Resource
    TableActionMapper tableMapper;

    @Resource
    MysqlCheckService mysqlCheckService;

    @Override
    public MySqlCreateTableStatement convertVoToStatement(TableParams tableParams) {

        MySqlCreateTableStatement statement = new MySqlCreateTableStatement();

        if (tableParams.getTableHeader().getTableName() != null && tableParams.getTableHeader().getTableName().contains("_")) {
            StringOptional.of(tableParams.getTableHeader().getTableName())
                    .trimToEmpty()
                    .trim("`")
                    .addFix("`").
                    ifNotEmpty(statement::setName);
        }
        else {
            StringOptional.of(tableParams.getTableHeader().getTableName())
                    .trimToEmpty()
                    .emptyMap(x -> faker.pokemon().name())
                    .replaceAll("[^a-zA-Z0-9]", "")
                    .lowerCamelToLowerUnderscore()
                    .addFix("`")
                    .ifNotEmpty(statement::setName);
        }

        StringOptional.of(tableParams.getTableHeader().getEngine())
                .ifNotBlank(engine -> statement.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("ENGINE"), new SQLIdentifierExpr(engine))));

        StringOptional.of(tableParams.getTableHeader().getCharset())
                .ifNotBlank(charSet -> statement.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("CHARSET"), new SQLIdentifierExpr(charSet))));


        StringOptional.of(tableParams.getTableHeader().getSortRuler())
                .ifNotBlank(sortRuler -> statement.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("COLLATE"), new SQLIdentifierExpr(sortRuler))));


        StringOptional.of(tableParams.getTableHeader().getComment())
                .ifNotBlank(comment -> statement.getTableOptions().add(new SQLAssignItem(new SQLIdentifierExpr("COMMENT"), new SQLIdentifierExpr("'" + comment + "'"))));

        statement.setIfNotExiists(tableParams.getTableHeader().isIfNotExist());

        // 填入行信息
        for (TableParams.TableColumn tableColumn : tableParams.getTableColumns()) {
            SQLColumnDefinition sqlColumnDefinition = new SQLColumnDefinition();
            statement.addColumn(sqlColumnDefinition);

            StringOptional.of(tableColumn.getRowName())
                    .lowerCamelToLowerUnderscore()
                    .trim("`")
                    .addFix("`")
                    .next(sqlColumnDefinition::setName);

            StringOptional.of(tableColumn.getComment())
                    .ifNotBlank(sqlColumnDefinition::setComment);

            StringOptional.of(tableColumn.getExtra())
                    .equalsAny(MysqlEnum.AUTO_INCREMENT.getRealParam())
                    .ifNotEmpty(row -> sqlColumnDefinition.setAutoIncrement(true));

            StringOptional.of(tableColumn.getExtra())
                    .equalsAny(MysqlEnum.CURRENT_TIMESTAMP_ON_UPDATE.getRealParam())
                    .ifNotEmpty(row -> sqlColumnDefinition.setOnUpdate(new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_TIMESTAMP)));

            if (!tableColumn.isAllowNull()) {
                sqlColumnDefinition.addConstraint(new SQLNotNullConstraint());
            }

            // 构造如Decimal(10,2)的数据
            String dataType = tableColumn.getDataType();
            SQLCharacterDataType sqlCharacterDataType = new SQLCharacterDataType(dataType);

            Optional.ofNullable(tableColumn.getParam1())
                    .ifPresent(x -> sqlCharacterDataType.getArguments().add(new SQLIntegerExpr(tableColumn.getParam1())));
            Optional.ofNullable(tableColumn.getParam2())
                    .ifPresent(x -> sqlCharacterDataType.getArguments().add(new SQLIntegerExpr(tableColumn.getParam2())));

            sqlColumnDefinition.setDataType(sqlCharacterDataType);

            if (Objects.equals(tableColumn.getDefault(), MysqlEnum.CURRENT_TIMESTAMP.getRealParam())) {
                sqlColumnDefinition.setDefaultExpr(new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_TIMESTAMP));
            }
            else if (StringUtils.equalsIgnoreCase("NULL", tableColumn.getDefault())) {
                sqlColumnDefinition.setDefaultExpr(new SQLNullExpr());
            }
            // 理论上需要判断是否是数字，但是实际看来应该字符串是万能的
            // 没有太好的方法
            else if (StringUtils.isNotBlank(tableColumn.getDefault())) {
                sqlColumnDefinition.setDefaultExpr(new SQLCharExpr(tableColumn.getDefault()));
            }

            if (StringUtils.isNotBlank(tableColumn.getComment())) {
                sqlColumnDefinition.setComment(tableColumn.getComment());
            }
        }

        for (TableParams.TableIndex tableIndex : tableParams.getTableIndices()) {
            MySqlKey mySqlKey;
            if (tableIndex.getIndexType().equals(MysqlEnum.PRIMARY_KEY.getRealParam())) {
                mySqlKey = new MySqlPrimaryKey();
                mySqlKey.getIndexDefinition().setType("primary");
                mySqlKey.getIndexDefinition().setKey(true);
            }
            else if (tableIndex.getIndexType().equals(MysqlEnum.UNIQUE_KEY.getRealParam())) {
                mySqlKey = new MySqlUnique();
                mySqlKey.getIndexDefinition().setType("unique");
                mySqlKey.getIndexDefinition().setKey(true);
            }
            else {
                mySqlKey = new MySqlKey();
                if (Objects.equals(tableIndex.getIndexType(), "FULLTEXT KEY")) {
                    mySqlKey.getIndexDefinition().getOptions().setIndexType("FULLTEXT");
                }
            }

            StringOptional.of(tableIndex.getIndexName())
                    .trim("`")
                    .trimToNull()
                    .addFix("`")
                    .ifNotBlank(mySqlKey::setName);

            // 完全不是标准实现
            // 偷懒了
            for (String indexColumn : tableIndex.getIndexColumns()) {
                String[] split = indexColumn.split("\\s");
                indexColumn = StringOptional.of(indexColumn)
                        .removeEnd("asc")
                        .removeEnd("desc")
                        .lowerCamelToLowerUnderscore()
                        .strip()
                        .addFix("`")
                        .append(" ", split[split.length - 1])
                        .get();
                SQLSelectOrderByItem sqlSelectOrderByItem = new SQLSelectOrderByItem();
                sqlSelectOrderByItem.setExpr(new SQLIdentifierExpr(indexColumn));
                mySqlKey.addColumn(sqlSelectOrderByItem);
            }

            // 如果只有一个列，那么不需要排序信息
            if (tableIndex.getIndexColumns().size() == 1) {
                SQLSelectOrderByItem sqlSelectOrderByItem = mySqlKey.getColumns().get(0);
                sqlSelectOrderByItem.setExpr(new SQLIdentifierExpr(sqlSelectOrderByItem.toString().replaceAll(" asc$", "")));
            }

            statement.getTableElementList().add(mySqlKey);

        }
        return statement;
    }

    @Override
    public TableParams convertStatToVo(String sql) throws NormalErrorException {
        if (StringUtils.isBlank(sql)) {
            ThrowUtils.throwIfNullOrBlack(sql, "字符串为空");
        }

        SQLStatement sqlStatement;
        try {
            sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        }
        catch (Exception e) {
            throw new NormalErrorException(ErrorEnum.NOT_ENOUGH_PARAMS, e.getMessage());
        }

        TableParams tableParams = new TableParams();
        tableParams.setTableHeader(new TableParams.TableHeader());
        tableParams.setTableIndices(new ArrayList<>());
        tableParams.setTableColumns(new ArrayList<>());

        if (sqlStatement instanceof MySqlCreateTableStatement statement) {
            StringOptional.of(statement.getTableName())
                    .trim("`")
                    .lowerUnderscoreToLowerCamel()
                    .next(tableParams.getTableHeader()::setTableName);

            tableParams.getTableHeader()
                    .setIfNotExist(statement.isIfNotExists());

            Optional.ofNullable(statement.getOption("ENGINE"))
                    .ifPresent(engine -> tableParams.getTableHeader().setEngine(engine.toString().split("\\s")[0]));

            StringOptional.stringOf(statement.getComment())
                    .trim("'")
                    .ifNotEmpty(tableParams.getTableHeader()::setComment);

            Optional.ofNullable(statement.getOption("CHARSET"))
                    .ifPresent(charSet -> tableParams.getTableHeader().setCharset(charSet.toString()));

            for (SQLColumnDefinition columnDefinition : statement.getColumnDefinitions()) {
                TableParams.TableColumn tableColumn = new TableParams.TableColumn();
                StringOptional.of(columnDefinition.getColumnName())
                        .trim("`")
                        .lowerUnderscoreToLowerCamel()
                        .next(tableColumn::setRowName);
                StringOptional.of(columnDefinition.getDataType().getName())
                        .upperCase()
                        .next(tableColumn::setDataType);

                StringOptional.stringOf(columnDefinition.getDefaultExpr())
                        .trim("'")
                        .ifNotEmpty(tableColumn::setDefault);

                List<SQLExpr> arguments = columnDefinition.getDataType().getArguments();
                if (!arguments.isEmpty()) {
                    tableColumn.setParam1(Integer.parseInt(arguments.get(0).toString()));
                }
                if (arguments.size() >= 2) {
                    tableColumn.setParam2(Integer.parseInt(arguments.get(1).toString()));
                }

                StringOptional.stringOf(columnDefinition.getComment())
                        .trim("'")
                        .ifNotEmpty(tableColumn::setComment);

                tableColumn.setAllowNull(true);
                for (SQLColumnConstraint constraint : columnDefinition.getConstraints()) {
                    if (Objects.equals(constraint.toString(), "NOT NULL")) {
                        tableColumn.setAllowNull(false);
                    }
                }

                Optional.ofNullable(columnDefinition.getOnUpdate())
                        .ifPresent(extra -> tableColumn.setExtra(extra.toString()));

                if (columnDefinition.isAutoIncrement()) {
                    tableColumn.setExtra("AUTO_INCREMENT");
                }
                StringOptional.stringOf(columnDefinition.getOnUpdate())
                        .equalsIgnoreCase("CURRENT_TIMESTAMP")
                        .ifNotEmpty(x -> tableColumn.setExtra("ON UPDATE CURRENT_TIMESTAMP"));

                tableParams.getTableColumns().add(tableColumn);
            }

            for (MySqlKey mysqlIndex : statement.getMysqlKeys()) {
                TableParams.TableIndex tableIndex = new TableParams.TableIndex();
                tableIndex.setIndexColumns(new ArrayList<>());
                tableParams.getTableIndices().add(tableIndex);
                SQLIndexDefinition indexDefinition = mysqlIndex.getIndexDefinition();

                StringOptional.stringOf(mysqlIndex.getIndexDefinition().getName())
                        .lowerUnderscoreToLowerCamel()
                        .trim("`")
                        .addFix("`")
                        .ifNotBlank(tableIndex::setIndexName);

                StringOptional.stringOf(mysqlIndex.getIndexDefinition().getType())
                        .equalsAny("PRIMARY", "UNIQUE")
                        .append(" KEY")
                        .ifNotEmpty(tableIndex::setIndexType);
                StringOptional.of(mysqlIndex.getIndexDefinition().getOptions().getIndexType())
                        .equalsIgnoreCase("FULLTEXT")
                        .append(" KEY").
                        ifNotEmpty(tableIndex::setIndexType);
                if (tableIndex.getIndexType() == null) {
                    tableIndex.setIndexName("KEY");
                }

                for (SQLSelectOrderByItem column : indexDefinition.getColumns()) {
                    String indexRow = column.toString();
                    if (!indexRow.matches(".+ ASC|.+ DESC")) {
                        indexRow += " asc";
                    }
                    StringOptional.of(indexRow)
                            .lowerCase()
                            .ifNotEmpty(tableIndex.getIndexColumns()::add);

                    // 主键定义
                    if (Objects.equals(tableIndex.getIndexType(), "PRIMARY KEY")) {
                        for (TableParams.TableColumn tableColumn : tableParams.getTableColumns()) {
                            StringOptional.of(indexRow)
                                    .lowerCase()
                                    .splitAndFirst()
                                    .trim("`")
                                    .equalsIgnoreCase(tableColumn.getRowName())
                                    .ifNotEmpty(x -> tableColumn.setPri(true));
                        }
                    }
                }
            }
            log.info(tableParams.toString());
            return tableParams;
        }
        throw new NormalErrorException(ErrorEnum.PARAM_NOT_SUPPORT, "不是建表语句的DDL！");
    }
    @Override
    public void createTable(String actionID, String userSchemaName, String sql) throws NormalErrorException {
        mysqlCheckService.checkActionIDSafe(actionID);
        mysqlCheckService.checkDataBaseNameSafe(userSchemaName);

        String realSchemaName = schemaService.getRealSchemaName(actionID, userSchemaName);
        ThrowUtils.throwIf(!mysqlCheckService.checkDatabaseExist(actionID, userSchemaName, false),
                ErrorEnum.EMPTY,
                realSchemaName + "数据库不存在");

        MySqlCreateTableStatement statement = sqlService.readCreateSql(sql);
        String tableName = StringUtils.strip(statement.getTableName(), "`");

        // 如果数据库存在那么跳过
        ThrowUtils.throwIf(mysqlCheckService.checkTableExist(actionID, userSchemaName, tableName, false),
                ErrorEnum.EXIST,
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
                ErrorEnum.EMPTY,
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
                ErrorEnum.EMPTY,
                String.format("操作的表格%s不存在！", tableName));

        String realSchemaName = schemaService.getRealSchemaName(actionID, databaseName);

        Map<String, String> params = tableMapper.getCreateDDL(realSchemaName, tableName);
        return params.getOrDefault("Create Table", null);
    }

}
