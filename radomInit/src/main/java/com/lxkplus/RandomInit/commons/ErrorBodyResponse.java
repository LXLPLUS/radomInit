package com.lxkplus.RandomInit.commons;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ErrorBodyResponse {
    int errorCode;
    String errorMessage;

    public ErrorBodyResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
