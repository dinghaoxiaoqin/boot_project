package com.rrk.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 连接图片服务器的配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "ftp")
public class ImageServiceConfig {

    private String address;

    private Integer port;

    private String username;

    private String password;

    private String bastPath;

    private String imageBaseUrl;

}
