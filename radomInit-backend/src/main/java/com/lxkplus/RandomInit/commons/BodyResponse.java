package com.lxkplus.RandomInit.commons;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BodyResponse<T> {
    int errorCode;
    String errorMessage;
    T data;
    String info;
    String version;

    /**
     * 正常处理的时候会生成
     * @param data 实际的信息，因为业务不同会生成不同的body
     * @param info 标识符，定义怎么解析
     */
    public BodyResponse(T data, Object info) {
        this.errorCode = 0;
        this.errorMessage = "OK!";
        this.data = data;
        this.info = info.toString();
    }

    public BodyResponse(T data, Object info, String version) {
        this.errorCode = 0;
        this.errorMessage = "OK!";
        this.data = data;
        this.info = info.toString();
        this.version = version;
    }
}
