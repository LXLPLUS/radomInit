package com.lxkplus.RandomInit.exception;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class NormalErrorException extends Exception{
    final int errorCode;
    final String errorMessage;

    public NormalErrorException(ErrorEnum errorEnum, String errorMessage) {
        this.errorCode = errorEnum.index;
        this.errorMessage = errorMessage;
    }

    public NormalErrorException(ErrorEnum errorEnum) {
        this.errorCode = errorEnum.index;
        this.errorMessage = errorEnum.errorName;
    }
}
