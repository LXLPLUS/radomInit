package com.lxkplus.RandomInit.service.impl;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.javafaker.Faker;
import com.google.common.base.CaseFormat;
import com.lxkplus.RandomInit.commons.StringOptional;
import com.lxkplus.RandomInit.enums.TypeMapper;
import com.lxkplus.RandomInit.model.VO.MybatisParams;
import com.lxkplus.RandomInit.model.VO.TableParams;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;

@Service
public class CodeBuilderImpl {

    @Resource
    TypeMapper typeMapper;

    @Resource
    Faker faker;

    public String mybatisPlusGather(TableParams tableParams, MybatisParams mybatisParams) {
        StringOptional.of(tableParams.getTableHeader().getTableName())
                .ifBlank(x -> tableParams.getTableHeader().setTableName(faker.pokemon().name()));
        String tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, tableParams.getTableHeader().getTableName());
        TypeSpec.Builder builder = TypeSpec.classBuilder(tableName).addModifiers(Modifier.PUBLIC);

        String lowerTableName = StringOptional.of(tableParams.getTableHeader().getTableName())
                .lowerCamelToLowerUnderscore().get();

        builder.addAnnotation(Data.class);
        if (mybatisParams.isDefaultTableName()) {
            AnnotationSpec tableNameAnnotation = AnnotationSpec.builder(TableName.class)
                    .addMember("value", "$L", lowerTableName)
                    .build();
            builder.addAnnotation(tableNameAnnotation);
        }

        // 写入参数
        for (TableParams.TableColumn tableColumn : tableParams.getTableColumns()) {
            String typeName = tableColumn.getDataType().toUpperCase();
            FieldSpec.Builder rowBuilder;
            rowBuilder = FieldSpec.builder(typeMapper.get(typeName,
                    mybatisParams.isBoxed(),
                    mybatisParams.isAllString()),
                    tableColumn.getRowName(),
                    Modifier.PRIVATE);

            if (mybatisParams.isAddComment()) {
                rowBuilder.addJavadoc(StringUtils.defaultIfBlank(tableColumn.getComment(), ""));
            }
            if (tableColumn.isPri() && mybatisParams.isDefaultTableName()) {
                AnnotationSpec rowAnnotation = AnnotationSpec.builder(TableId.class)
                        .addMember("type", "$N", "IdType.AUTO")
                        .build();
                rowBuilder.addAnnotation(rowAnnotation);
            }
            if (!tableColumn.isAllowNull() && mybatisParams.isValid()) {
                rowBuilder.addAnnotation(NotNull.class);
            }
            builder.addField(rowBuilder.build());
        }

        return JavaFile.builder("", builder.build())
                .build().toString();
    }

}
