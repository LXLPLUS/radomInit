package com.lxkplus.RandomInit.aop;

import com.lxkplus.RandomInit.exception.UnauthorizedError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Value("${randomInit.debug-mode}")
    Boolean debugMode;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (debugMode) {
            log.info("url: " + request.getRequestURI());
            request.getSession().getAttributeNames().asIterator().forEachRemaining(x -> log.info("session" + x + ":" + request.getSession().getAttribute(x)));
            return true;
        }
        if (request.getSession(true).getAttribute("actionID") == null) {
            throw new UnauthorizedError();
        }
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, @Nullable Exception ex) throws Exception {
    }

}
