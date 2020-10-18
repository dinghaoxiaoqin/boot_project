package com.rrk.common.modules.user.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dinghao
 * @since 2020-02-27
 */
public class UserConnection extends Model<UserConnection> implements Serializable {

private static final long serialVersionUID=1L;

    private String userId;

    private String providerId;

    private String providerUserId;

    private Integer rank;

    private String displayName;

    private String profileUrl;

    private String imageUrl;

    private String accessToken;

    private String secret;

    private String refreshToken;

    private Long expireTime;


    public String getUserId() {
        return userId;
    }

    public UserConnection setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getProviderId() {
        return providerId;
    }

    public UserConnection setProviderId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public UserConnection setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
        return this;
    }

    public Integer getRank() {
        return rank;
    }

    public UserConnection setRank(Integer rank) {
        this.rank = rank;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserConnection setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public UserConnection setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UserConnection setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public UserConnection setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public UserConnection setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public UserConnection setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public UserConnection setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "UserConnection{" +
        "userId=" + userId +
        ", providerId=" + providerId +
        ", providerUserId=" + providerUserId +
        ", rank=" + rank +
        ", displayName=" + displayName +
        ", profileUrl=" + profileUrl +
        ", imageUrl=" + imageUrl +
        ", accessToken=" + accessToken +
        ", secret=" + secret +
        ", refreshToken=" + refreshToken +
        ", expireTime=" + expireTime +
        "}";
    }
}
