package com.rrk.common.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dinghao
 * @since 2020-02-25
 */
@TableName("oauth_client_details")
@Data
public class OauthClientDetails extends Model<OauthClientDetails> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 接入的资源列表
     */
    private String resourceIds;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 资源对应的权限
     */
    private String scope;

    private String authorizedGrantTypes;

    private String webServerRedirectUri;

    private String authorities;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    /**
     * 创建时间
     */
    private Date createTime;

    private Integer archived;

    private Integer trusted;


    public Long getId() {
        return id;
    }

    public OauthClientDetails setId(Long id) {
        this.id = id;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public OauthClientDetails setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public OauthClientDetails setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public OauthClientDetails setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public OauthClientDetails setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public OauthClientDetails setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
        return this;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public OauthClientDetails setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
        return this;
    }

    public String getAuthorities() {
        return authorities;
    }

    public OauthClientDetails setAuthorities(String authorities) {
        this.authorities = authorities;
        return this;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public OauthClientDetails setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
        return this;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public OauthClientDetails setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public OauthClientDetails setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getArchived() {
        return archived;
    }

    public OauthClientDetails setArchived(Integer archived) {
        this.archived = archived;
        return this;
    }

    public Integer getTrusted() {
        return trusted;
    }

    public OauthClientDetails setTrusted(Integer trusted) {
        this.trusted = trusted;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OauthClientDetails{" +
        "id=" + id +
        ", clientId=" + clientId +
        ", resourceIds=" + resourceIds +
        ", clientSecret=" + clientSecret +
        ", scope=" + scope +
        ", authorizedGrantTypes=" + authorizedGrantTypes +
        ", webServerRedirectUri=" + webServerRedirectUri +
        ", authorities=" + authorities +
        ", accessTokenValidity=" + accessTokenValidity +
        ", refreshTokenValidity=" + refreshTokenValidity +
        ", createTime=" + createTime +
        ", archived=" + archived +
        ", trusted=" + trusted +
        "}";
    }
}
