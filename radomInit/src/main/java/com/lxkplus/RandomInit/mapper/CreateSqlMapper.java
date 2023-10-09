package com.lxkplus.RandomInit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxkplus.RandomInit.dto.CreateSql;
import com.lxkplus.RandomInit.dto.TableMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【t_create_sql(建表语句记录)】的数据库操作Mapper
* @createDate 2023-10-08 19:16:30
* @Entity com.lxkplus.RandomInit.dto.CreateSql
*/
public interface CreateSqlMapper extends BaseMapper<CreateSql> {

    List<TableMessage> getAllByActionIdAndIsDelete(@Param("actionId") String actionId);
}




