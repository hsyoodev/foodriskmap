package com.hangaramit.foodriskmap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hangaramit.foodriskmap.interceptor.SignInCheckInterceptor;

@Configuration

public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private SignInCheckInterceptor signInCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signInCheckInterceptor).addPathPatterns("/board/write");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
