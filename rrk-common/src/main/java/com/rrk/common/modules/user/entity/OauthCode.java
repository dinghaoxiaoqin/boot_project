package com.rrk.common.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

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
@TableName("oauth_code")
public class OauthCode extends Model<OauthCode> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 授权码
     */
    private String code;

    private String authentication;


    public Long getId() {
        return id;
    }

    public OauthCode setId(Long id) {
        this.id = id;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public OauthCode setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCode() {
        return code;
    }

    public OauthCode setCode(String code) {
        this.code = code;
        return this;
    }

    public String getAuthentication() {
        return authentication;
    }

    public OauthCode setAuthentication(String authentication) {
        this.authentication = authentication;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OauthCode{" +
        "id=" + id +
        ", createTime=" + createTime +
        ", code=" + code +
        ", authentication=" + authentication +
        "}";
    }
}
