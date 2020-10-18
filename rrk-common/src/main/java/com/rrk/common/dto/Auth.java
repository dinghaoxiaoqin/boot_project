package com.rrk.common.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * 安全认证
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dinghao")
public class Auth implements Serializable {

    /**
     * token过期时间（分钟）
     */
    private Integer tokenExpireTime;
    /**
     * 用户选择保存登录状态对应token过期时间（天）
     */
    private Integer saveLoginTime;
    /**
     * 限制用户登陆错误次数（次）
     */
    private Integer loginTimeLimit;
    /**
     * 错误超过次数后多少分钟后才能继续登录（分钟）
     */
    private Integer loginAfterTime;
}
