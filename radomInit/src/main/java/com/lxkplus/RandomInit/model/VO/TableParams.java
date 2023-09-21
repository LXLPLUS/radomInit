package com.lxkplus.RandomInit.model.VO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class TableParams {
    @NotNull(message = "表头信息不能为空")
    TableHeader tableHeader;
    @NotNull(message = "最少应该有一列")
    @Size(min = 1)
    List<TableColumn> tableColumns;
    List<TableIndex> tableIndices;

    @Data
    public static class TableIndex {
        String indexName;
        String indexType;
        List<String> indexColumns;
    }

    @Data
    public static class TableColumn {
        @NotNull
        String rowName;
        String dataType;
        boolean pri;
        boolean allowNull;
        String defaultMessage;
        String extra;
        String comment;
        Integer param1;
        Integer param2;

        public String getDefault() {
            return defaultMessage;
        }

        public void setDefault(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }
    }

    @Data
    public static class TableHeader {
        @NotNull(message = "表头不能为空")
        String tableName;
        String engine;
        String charset;
        String sortRuler;
        String comment;
        boolean ifNotExist;
    }
}
