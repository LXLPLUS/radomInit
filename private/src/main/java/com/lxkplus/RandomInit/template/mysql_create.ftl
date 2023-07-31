CREATE TABLE IF NOT EXISTS `${tableName}` (
    <#list mysqlRows as mysqlRow>
        ${mysqlRow}
    </#list>
)ENGINE=${engineName} DEFAULT CHARSET=${charSet}