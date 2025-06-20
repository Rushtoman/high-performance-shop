package com.example.shop.config;

import com.example.shop.interceptor.JwtInterceptor;
import com.example.shop.interceptor.RateLimitInterceptor;

import io.micrometer.common.lang.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/user/login", "/api/order/create");
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login", "/api/user/register", "/swagger-ui/**", "/v3/api-docs/**");
    }
} 