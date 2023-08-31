package com.lxkplus.RandomInit.model;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.codec.digest.Md5Crypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class MysqlColumn {
    String userSchemaName;
    String tableName;
    String columnName;
    String dataType;
    String columnKey;
    String columnDefault;
    String extra;
    String comment;

    public String getDigest() {
        try {
            byte[] bytes = this.toString().getBytes(StandardCharsets.UTF_8);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(bytes);
            return Md5Crypt.apr1Crypt(digest, "randomInit").substring(30);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }



    public String getUserSchemaName() {
        if (userSchemaName == null) {
            return null;
        }
        return userSchemaName.split("_", 4)[3];
    }

    /**
     * 获取一个列的摘要
     * @param mysqlColumns 列表名称
     * @param digest 摘要
     * @return 返回符合摘要信息的列表
     */
    static public List<MysqlColumn> getListByDigest(List<MysqlColumn> mysqlColumns, String digest) {
        return mysqlColumns.stream().filter(x -> x.getDigest().equals(digest)).collect(Collectors.toList());
    }
}
