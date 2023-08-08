package com.lxkplus.RandomInit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lxkplus.RandomInit.enums.ErrorEnum;
import com.lxkplus.RandomInit.exception.NormalErrorException;
import com.lxkplus.RandomInit.model.MysqlColumn;
import com.lxkplus.RandomInit.model.MysqlCreate;
import com.lxkplus.RandomInit.model.MysqlIndex;
import com.lxkplus.RandomInit.model.RandomParam;
import com.lxkplus.RandomInit.service.TemplateService;
import com.lxkplus.RandomInit.utils.JsonUtils;
import jakarta.annotation.Resource;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Resource
    JsonUtils jsonUtils;


    @Override
    public String mysqlTableBuilder(MysqlCreate mysqlCreate) throws JsonProcessingException {

        // 解析注释来实现生成解析规则
        LinkedHashMap<String, RandomParam> buildRulerMap = new LinkedHashMap<>();
        for (MysqlColumn mysqlColumn : mysqlCreate.getMysqlColumnDefinitionList()) {
            if (mysqlColumn.getBuildRuler() != null) {
                String rowName = StringUtils.strip(mysqlColumn.getColumnName(), "`");
                buildRulerMap.put(rowName, mysqlColumn.getBuildRuler());
            }
        }
        String head = jsonUtils.yamlMapper.writeValueAsString(buildRulerMap);
        head = "/*\n" + head.replaceFirst("---\n", "") + "*/\n";

        CreateTable createTable = new CreateTable();
        Table table = new Table(mysqlCreate.getTableName());
        // 指定namespace
        if (mysqlCreate.getDatabase() != null) {
            table.setSchemaName(mysqlCreate.getDatabase());
        }
        createTable.setTable(table);
        createTable.setIfNotExists(true);
        createTable.setTableOptionsStrings(new ArrayList<>());
        createTable.getTableOptionsStrings().addAll(List.of("ENGINE", "=", mysqlCreate.getEngine()));
        createTable.getTableOptionsStrings().addAll(List.of("DEFAULT", "CHARSET", "=", mysqlCreate.getCharSet()));
        createTable.getTableOptionsStrings().addAll(List.of("COLLATE", "=", mysqlCreate.getCollate()));
        createTable.getTableOptionsStrings().addAll(List.of("COMMENT", "=", mysqlCreate.getComment()));

        for (MysqlColumn mysqlColumn : mysqlCreate.getMysqlColumnDefinitionList()) {
            ColumnDefinition columnDefinition = new ColumnDefinition();
            columnDefinition.setColumnName(mysqlColumn.getColumnName());

            ColDataType colDataType = new ColDataType();
            colDataType.setDataType(mysqlColumn.getType().toLowerCase());
            if (mysqlColumn.getTypeArgument() != null) {
                colDataType.setArgumentsStringList(new ArrayList<>(mysqlColumn.getTypeArgument()));
            }
            columnDefinition.setColDataType(colDataType);

            if (mysqlColumn.isUpdateTime()) {
                mysqlColumn.setDefaultMessage("");
                mysqlColumn.setInsertTime(false);
                columnDefinition.addColumnSpecs("DEFAULT", "CURRENT_TIMESTAMP", "ON", "UPDATE CURRENT_TIMESTAMP");
            }
            if (mysqlColumn.isInsertTime()) {
                mysqlColumn.setDefaultMessage("");
                columnDefinition.addColumnSpecs("DEFAULT", "CURRENT_TIMESTAMP");
            }

            if (StringUtils.isBlank(mysqlColumn.getDefaultMessage())) {
                mysqlColumn.setComment("");
            }
            else if ("NULL".equalsIgnoreCase(mysqlColumn.getDefaultMessage())) {
                columnDefinition.addColumnSpecs("DEFAULT", "NULL");
            }
            else {
                // 转化为默认的字符格式
                String defaultMessage = StringUtils.strip(mysqlColumn.getDefaultMessage(), "\"'");
                columnDefinition.addColumnSpecs("DEFAULT", "'" + defaultMessage + "'");
            }

            if (mysqlColumn.isNotNull()) {
                columnDefinition.addColumnSpecs("NOT", "NULL");
            }
            if (mysqlColumn.isAutoIncrease()) {
                columnDefinition.addColumnSpecs("AUTO_INCREMENT");
            }

            createTable.addColumnDefinitions(columnDefinition);
        }

        // 索引转化
        List<MysqlIndex> mysqlIndexList = mysqlCreate.getMysqlIndexList();
        for (MysqlIndex mysqlIndex : mysqlIndexList) {
            Index index = new Index();
            index.setName(mysqlIndex.getIndexName());
            index.setType(mysqlIndex.getIndexType());
            index.setColumnsNames(mysqlIndex.getRowList());
            createTable.addIndexes(index);
        }
        return head + "\n" + sqlToString(createTable.toString());
    }

    @Override
    public MysqlCreate sqlParser(String sql) throws NormalErrorException, JSQLParserException, JsonProcessingException {
        if (StringUtils.isBlank(sql)) {
            throw new NormalErrorException(ErrorEnum.NotHaveAnyData, "sql不存在");
        }
        Statement parse = CCJSqlParserUtil.parse(sql);
        if (parse instanceof CreateTable createTable) {
            MysqlCreate mysqlCreate = new MysqlCreate();
            mysqlCreate.setTableName(createTable.getTable().getName());
            String schemaName = createTable.getTable().getSchemaName();
            mysqlCreate.setDatabase(schemaName);
            List<String> options = createTable.getTableOptionsStrings();

            for (int i = 2; i < options.size(); i++) {
                if ("COMMENT".equalsIgnoreCase(options.get(i - 2)) && "=".equals(options.get(i - 1))) {
                    mysqlCreate.setComment(StringUtils.strip(options.get(i), "'"));
                }
                if ("ENGINE".equalsIgnoreCase(options.get(i - 2)) && "=".equals(options.get(i - 1))) {
                    mysqlCreate.setEngine(options.get(i));
                }
                if ("CHARSET".equalsIgnoreCase(options.get(i - 2)) && "=".equals(options.get(i - 1))) {
                    mysqlCreate.setCharSet(options.get(i));
                }
                if ("COLLATE".equalsIgnoreCase(options.get(i - 2)) && "=".equals(options.get(i - 1))) {
                    mysqlCreate.setCollate(options.get(i));
                }
            }

            for (ColumnDefinition columnDefinition : createTable.getColumnDefinitions()) {
                MysqlColumn mysqlColumn = new MysqlColumn();
                mysqlColumn.setColumnName(columnDefinition.getColumnName());
                mysqlColumn.setType(columnDefinition.getColDataType().getDataType().toLowerCase());
                mysqlColumn.setTypeArgument(columnDefinition.getColDataType().getArgumentsStringList());

                List<String> columnSpecs = columnDefinition.getColumnSpecs();
                for (int i = 1; i < columnSpecs.size(); i++) {
                    if ("DEFAULT".equalsIgnoreCase(columnSpecs.get(i - 1))) {
                        mysqlColumn.setDefaultMessage(columnSpecs.get(i));
                        if (!columnSpecs.get(i).startsWith("'")) {
                            mysqlColumn.setDefaultMessage(columnSpecs.get(i));
                        }
                    }
                    if ("DEFAULT".equalsIgnoreCase(columnSpecs.get(i - 1))
                            && "CURRENT_TIMESTAMP".equalsIgnoreCase(columnSpecs.get(i))) {
                        mysqlColumn.setInsertTime(true);
                        mysqlColumn.setDefaultMessage("");
                    }
                    if ("UPDATE".equalsIgnoreCase(columnSpecs.get(i - 1))
                            && "CURRENT_TIMESTAMP".equalsIgnoreCase(columnSpecs.get(i))) {
                        mysqlColumn.setUpdateTime(true);
                        mysqlColumn.setComment("");
                    }

                    if ("NOT".equalsIgnoreCase(columnSpecs.get(i - 1))
                            && "NULL".equalsIgnoreCase(columnSpecs.get(i))) {
                        mysqlColumn.setNotNull(true);
                    }

                    if ("COMMENT".equalsIgnoreCase(columnSpecs.get(i - 1))) {
                        mysqlColumn.setComment(columnSpecs.get(i));
                    }

                    if ("AUTO_INCREMENT".equalsIgnoreCase(columnSpecs.get(i))) {
                        mysqlColumn.setAutoIncrease(true);
                    }
                }
                mysqlCreate.getMysqlColumnDefinitionList().add(mysqlColumn);
            }
            for (Index index : createTable.getIndexes()) {
                MysqlIndex mysqlIndex = new MysqlIndex();
                mysqlIndex.setIndexType(index.getType());
                mysqlIndex.setIndexName(index.getName());
                mysqlIndex.setRowList(index.getColumns().stream().map(x -> x.columnName).collect(Collectors.toList()));
                mysqlCreate.getMysqlIndexList().add(mysqlIndex);
            }

            // 通过注释来标注生成规则
            // 如果无法获取注释那么跳过

            String sqlComment = StringUtils.substringBetween(sql, "/*", "*/");

            if (StringUtils.isNotBlank(sqlComment)) {
                Map<String, RandomParam> stringStringMap =
                        jsonUtils.yamlMapper.readValue(sqlComment.strip(), new TypeReference<>() {});

                for (MysqlColumn mysqlColumn : mysqlCreate.getMysqlColumnDefinitionList()) {
                    String rowName = StringUtils.strip(mysqlColumn.getColumnName(), "`");
                    if (stringStringMap.containsKey(rowName)) {
                        mysqlColumn.setBuildRuler(stringStringMap.get(rowName));
                    }
                }
            }
            return mysqlCreate;
        }
        throw new NormalErrorException(ErrorEnum.NotHaveAnyData, "没有解析出建表语句");
    }

    String sqlToString(String sql) {
        String[] split = sql.replace(", ", ",\n")
                .replace(") ENGINE", "\n) ENGINE")
                .replaceFirst("\\(", "(\n")
                .replace("`,\n`", "`, `")
                .split("\n");
        for (int i = 0; i < split.length; i++) {
            if (i > 0 && i < split.length - 1) {
                split[i] = "  " + split[i];
            }
        }
        return StringUtils.join(split, "\n");
    }
}
