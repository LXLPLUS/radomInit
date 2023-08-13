package com.lxkplus.RandomInit.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.springframework.stereotype.Component;

@Component
public class SqlBuildUtils {
    public Statement sqlAlias(String sql, String aliasName) throws JSQLParserException {
        Table table = new Table(aliasName);
        Alias alias = new Alias(aliasName);
        alias.setUseAs(false);

        Statement parse = CCJSqlParserUtil.parse(sql);
        if (parse instanceof Select select) {
            SelectBody selectBody = select.getSelectBody();

            if (selectBody instanceof PlainSelect plainSelect) {
                for (SelectItem selectItem : plainSelect.getSelectItems()) {

                    // 对列进行更名
                    if (selectItem instanceof SelectExpressionItem selectExpressionItem) {
                        Expression expression = selectExpressionItem.getExpression();
                        if (expression instanceof Column column) {
                            column.setTable(table);
                        }
                    }

                    // 专门处理 *
                    if (selectItem instanceof AllTableColumns allTableColumns) {
                        allTableColumns.setTable(table);
                    }
                }
                FromItem fromItem = plainSelect.getFromItem();

                // 对表进行更名
                if (fromItem instanceof Table sourceTable) {
                    sourceTable.setAlias(alias);
                }
            }
        }
        return parse;
    }
}
