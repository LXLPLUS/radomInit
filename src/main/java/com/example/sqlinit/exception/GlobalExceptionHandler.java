package com.example.sqlinit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NormalErrorException.class)
    public BodyResponse<?> NormalErrorExceptionHandler(NormalErrorException e) {
        log.warn("出现错误，错误码为：{}, 错误原因为 {}", e.getErrorCode(), e.getErrorMessage());
        return new BodyResponse<>(e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(Exception.class)
    public BodyResponse<?> ExceptionHandler(Exception e) {
        log.warn("出现错误，错误原因为 {}", e.getMessage());
        return new BodyResponse<>(-1, e.getMessage());
    }
}
