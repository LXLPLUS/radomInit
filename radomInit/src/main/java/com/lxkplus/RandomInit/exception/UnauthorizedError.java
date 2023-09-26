package com.lxkplus.RandomInit.exception;

import com.lxkplus.RandomInit.enums.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "未登录")
public class UnauthorizedError extends NormalErrorException{
    public UnauthorizedError() {
        super(ErrorEnum.UNAUTHORIZED, "未登录");
    }
}
