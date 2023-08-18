package com.lxkplus.RandomInit.mapper;

import com.lxkplus.RandomInit.model.ViewInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ViewMapper {

    @Insert("create view randomInit_temp_${actionID}_default.${viewName} as ${selectSql}")
    void createView(String actionID, String viewName, String selectSql);

    @Select("""
            select
                TABLE_SCHEMA as `userDatabaseName`,
                TABLE_NAME as `tableName`,
                VIEW_DEFINITION as `sql`
            from information_schema.VIEWS
            where TABLE_SCHEMA like concat('randomInit_temp_', #{userID}, '_%')
            """)
    List<ViewInfo> getView(String actionID);
}
