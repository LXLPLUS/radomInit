package com.lxkplus.RandomInit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxkplus.RandomInit.model.BuilderRuler;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BuilderMapper extends BaseMapper<BuilderRuler> {

    @Select("select * from builder_ruler where action_id = #{actionID}")
    List<BuilderRuler> getBuildRuler(String actionID);

    @Select("select * from builder_ruler where action_id = #{actionID} and database_name = #{databaseName}")
    List<BuilderRuler> getBuilderRuler(String actionID, String databaseName);

    @Select("select * from builder_ruler where action_id = #{actionID} and database_name = #{databaseName} and table_name = #{tableName}")
    List<BuilderRuler> getBuilderRuler(String actionID, String databaseName, String tableName);

    @Delete("delete from builder_ruler where action_id = #{actionID}")
    void deleteByActionID(String actionID);
}
