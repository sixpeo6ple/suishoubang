package com.xyy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域的域名
                .allowedOriginPatterns("*")
                // 设置允许Cookie
                .allowCredentials(true)
                // 设置运行的请求方式
                .allowedMethods("*")
                // 设置运行的请求头
                .allowedHeaders("*")
                // 跨域允许时间,比如POST请求会先发送询问请求,如果本次请求允许
            	// 则在指定时间内都允许,不用发送询问请求
                .maxAge(3600);
    }
}