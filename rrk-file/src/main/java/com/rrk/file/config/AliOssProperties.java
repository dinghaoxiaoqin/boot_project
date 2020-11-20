package com.rrk.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 获取阿里云对象存储的oss属性配置
 * @author dh
 * @date 2020 11-10
 */
@Component
@ConfigurationProperties(value = "alioss")
public class AliOssProperties {

    /**
     * 外网访问的url
     */
    private String ossUrl;
    /**
     * accessKeyId
     */
    private String accessKeyId;
    /**
     * secret
     */
    private String accessKeySecret;

    /**
     * 创建的存储空间名称
     */
    private String bucket;

    public String getOssUrl() {
        return ossUrl;
    }

    public void setOssUrl(String ossUrl) {
        this.ossUrl = ossUrl;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }





}
