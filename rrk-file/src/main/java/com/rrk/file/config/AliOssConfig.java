package com.rrk.file.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里对象存储oss配置
 * @date 2020 11-10
 * @author dh
 */
@Configuration
public class AliOssConfig {

    @Autowired
    private AliOssProperties aliOssProperties;

    @Bean
    public OSSClient ossClient(){
        return new OSSClient(aliOssProperties.getOssUrl(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret());
    }
}
