package com.rrk.manage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 抖音的相关属性配置信息
 */
@Component
@ConfigurationProperties(prefix = "douyin")
public class DouyinProperties {

    private String appSecrent;

    private String key;

    private String url;

    public String getAppSecrent() {
        return appSecrent;
    }

    public void setAppSecrent(String appSecrent) {
        this.appSecrent = appSecrent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
