package com.example.sqlinit.exception;

import lombok.Getter;

@Getter
public class NormalErrorException extends Exception{
    int errorCode;
    String errorMessage;

    public NormalErrorException(ErrorEnum errorEnum, String errorMessage) {
        this.errorCode = errorEnum.index;
        this.errorMessage = errorMessage;
    }

    public NormalErrorException(ErrorEnum errorEnum) {
        this.errorCode = errorEnum.index;
        this.errorMessage = errorEnum.errorName;
    }
}
