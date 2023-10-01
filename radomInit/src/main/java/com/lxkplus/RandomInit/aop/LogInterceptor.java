package com.lxkplus.RandomInit.aop;

import com.lxkplus.RandomInit.commons.Timer;
import com.lxkplus.RandomInit.exception.UnauthorizedError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Value("${randomInit.debug-mode}")
    boolean debugMode;

    Timer timer;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (debugMode) {
            timer = new Timer();
            timer.flushStartTime();
            log.info("url: " + request.getRequestURI());
        }
        if (request.getSession(true).getAttribute("actionID") == null) {
            throw new UnauthorizedError();
        }
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, @Nullable Exception ex) throws Exception {
        if (debugMode) {
            timer.flushFinishTime();
            log.info("耗费的时间为" + timer.getSpendMillis() + "ms");
        }
    }
}
