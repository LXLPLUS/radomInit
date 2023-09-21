package com.lxkplus.RandomInit.exception;

import com.lxkplus.RandomInit.commons.ErrorBodyResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${randomInit.debug-mode}")
    Boolean debugMode;

    @ExceptionHandler(NormalErrorException.class)
    public ErrorBodyResponse NormalErrorExceptionHandler(NormalErrorException e) {
        return new ErrorBodyResponse(e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(JSQLParserException.class)
    public ErrorBodyResponse NormalErrorExceptionHandler(JSQLParserException e) {
        return new ErrorBodyResponse(-2, "sql解析失败！");
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorBodyResponse BlindExceptionHandler(Exception e) {
        log.warn("出现错误，错误原因为 {}", e.getMessage());
        if (debugMode) {
            e.printStackTrace();
            return new ErrorBodyResponse(-3, e.getMessage());
        }
        return new ErrorBodyResponse(-3, "缺乏必要参数");
    }

    @ExceptionHandler(Exception.class)
    public ErrorBodyResponse ExceptionHandler(Exception e) {
        log.warn("出现错误，错误原因为 {}", e.getMessage());
        if (debugMode) {
            e.printStackTrace();
            return new ErrorBodyResponse(-1, e.getMessage());
        }
        return new ErrorBodyResponse(-1, "内部错误");
    }
}
