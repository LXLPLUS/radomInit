package com.lxkplus.RandomInit.mapper;

import com.lxkplus.RandomInit.model.ViewInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ViewMapper {

    @Select("""
            select
                TABLE_SCHEMA as `userSchemaName`,
                TABLE_NAME as `tableName`,
                VIEW_DEFINITION as `sql`
            from information_schema.VIEWS
            where TABLE_SCHEMA like concat('randomInit_temp_', #{userID}, '_%')
            """)
    List<ViewInfo> getView(String actionID);
}
