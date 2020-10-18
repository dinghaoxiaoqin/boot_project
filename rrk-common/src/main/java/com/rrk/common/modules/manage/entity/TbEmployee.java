package com.rrk.common.modules.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dinghao
 * @since 2020-04-25
 */
public class TbEmployee extends Model<TbEmployee> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 员工id
     */
    private Long employeeId;

    /**
     * 员工账号
     */
    private String username;

    /**
     * 员工密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String icon;

    /**
     * 管理员手机号
     */
    private String phone;
    /**
     * 0启用 1禁用
     */
    private Integer isUse;

    /**
     * 管理员地址
     */
    private String employeeAddress;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Long getId() {
        return id;
    }

    public TbEmployee setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public TbEmployee setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public TbEmployee setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public TbEmployee setPassword(String password) {
        this.password = password;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbEmployee setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbEmployee setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbEmployee{" +
        "id=" + id +
        ", employeeId=" + employeeId +
        ", username=" + username +
        ", password=" + password +
        ", icon=" + icon +
        ", phone=" + phone +
        ", isUse=" + isUse +
        ", employeeAddress=" + employeeAddress +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
