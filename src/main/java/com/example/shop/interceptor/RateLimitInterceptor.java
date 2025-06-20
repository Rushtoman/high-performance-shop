package com.example.shop.interceptor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

/**
 * 接口防刷拦截器，基于Redis统计IP+接口单位时间内请求次数，超限则拦截
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    // 每个IP每个接口每分钟最多允许的请求数
    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final int EXPIRE_SECONDS = 60;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        // 获取当前请求的URI，用于与IP一起作为限流的唯一标识
        String uri = request.getRequestURI();
        String key = "rate_limit:" + ip + ":" + uri;
        // 自增计数
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            // 第一次请求，设置过期时间
            redisTemplate.expire(key, EXPIRE_SECONDS, java.util.concurrent.TimeUnit.SECONDS);
        }
        if (count != null && count > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(429); // 429 就是给出原因状态码，太多请求了
            response.getWriter().write("请求过于频繁，请稍后再试");
            return false;
        }
        return true;
    }
} 