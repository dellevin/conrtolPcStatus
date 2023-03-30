package com.wonder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsConfig {
    /**
     * 支持跨域请求的请求头信息字段
     */
    private String[] allowedHeaders = {"*"};
    /**
     * 支持跨域请求的方法
     */
    private String[] allowedMethods = {"POST", "GET", "PUT", "DELETE", "OPTIONS", "HEAD"};
    /**
     * 允许跨域请求的域名
     */
    private String[] allowedOriginPatterns = {"*"};
    /**
     * 是否允许发送Cookie
     */
    private boolean allowCredentials = true;
    /**
     * 本次请求的有效期
     */
    private long maxAge = 1800L;
}
