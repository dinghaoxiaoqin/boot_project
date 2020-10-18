package com.rrk.common.modules.user.entity;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rrk.common.constant.NumConstants;
import com.rrk.common.constant.UserContants;
import com.rrk.common.utils.ToolUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author dinghao
 * @since 2020-02-08
 */
@Data
@TableName("tb_user")
public class TbUser extends Model<TbUser> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 第三方用户id
     */
    private String sourceUid;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String userSrc;

    /**
     * 0无效用户，1是有效用户
     */
    private Integer enabled;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 用户等级(0 普通用户，1普通会员，2高级会员)
     */
    private Integer userRank;
    /**
     * 用户来源（0，手机，1，微博，2，qq,3支付宝）
     */
    private Integer sourceType;
    /**
     * 第三方授权码
     */
    private String sourceCode;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    public TbUser() {

    }

    public TbUser(Map<String, Object> uMap) {
        this.sourceUid = Convert.toStr(uMap.get("openid"));
        this.userSrc = Convert.toStr(uMap.get("headimgurl"));
        this.username = Convert.toStr(uMap.get("nickname"));
        this.nickName = Convert.toStr(uMap.get("nickname"));
        this.sourceType = NumConstants.SOURCE_WEIXIN;
        this.userRank = UserContants.NOMAL_ROLE;
        this.enabled = UserContants.USER_ENABLE;
        this.userId = ToolUtil.getLongNum();
        this.createTime = new Date();
    }

    public Long getUserId() {
        return userId;
    }

    public TbUser setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public TbUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public TbUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbUser setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public TbUser setEnabled(Integer enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public TbUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbUser setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public TbUser setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getUserSrc() {
        return userSrc;
    }

    public TbUser setUserSrc(String userSrc) {
        this.userSrc = userSrc;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserRank() {
        return userRank;
    }

    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "TbUser{" +
                "id=" + id +
                "userId=" + userId +
                "sourceUid=" + sourceUid+
                ", username=" + username +
                ", password=" + password +
                ", enabled=" + enabled +
                ", userSrc=" + userSrc +
                ", nickName=" + nickName +
                ", phone=" + phone +
                ", sourceType=" + sourceType +
                ", sourceCode=" + sourceCode +
                ", userRank=" + userRank +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
