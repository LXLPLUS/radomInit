package com.lxkplus.RandomInit.template;

import lombok.Data;
import lombok.ToString;

import java.util.List;

public class MysqlCreate {

    private static final String TEMPLATE_PATH = "src/main/java/com/lxkplus/RandomInit/template/mysql_create.ftl";
    String tableName;
    List<mysqlRow> mysqlRows;
    String engineName;
    String charSet;
}

@Data
@ToString
class mysqlRow {
    String RowName;
    String typeName;
    int len;
    Boolean isNULL;
    String defaultMessage;
    String commit;


}
