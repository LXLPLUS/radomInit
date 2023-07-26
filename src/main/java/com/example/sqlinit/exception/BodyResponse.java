package com.example.sqlinit.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BodyResponse<T> {
    int errorCode;
    String errorMessage;
    T data;
    String info;

    BodyResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = null;
        this.info = "error";
    }

    /**
     * 正常处理的时候会生成
     * @param data 实际的信息，因为业务不同会生成不同的body
     * @param info 标识符，定义怎么解析
     */
    public BodyResponse(T data, String info) {
        this.errorCode = 0;
        this.errorMessage = "";
        this.data = data;
        this.info = info;
    }
}
