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
public class TbPermission extends Model<TbPermission> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限id
     */
    private Long permissionId;
    /**
     * 菜单父id
     */
    private Long parentId;
    /**
     * 菜单的图标
     */
    private String icon;

    /**
     * 前端名称
     */
    private String name;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限url
     */
    private String permissionUrl;

    /**
     * 是否可用（0，可用，1 不可用）
     */
    private Integer isUse;
    /**
     * 菜单排序
     */
    private Integer orderCount;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public TbPermission setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public TbPermission setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public TbPermission setPermissionName(String permissionName) {
        this.permissionName = permissionName;
        return this;
    }

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public TbPermission setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
        return this;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public TbPermission setIsUse(Integer isUse) {
        this.isUse = isUse;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbPermission setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbPermission setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbPermission{" +
        "id=" + id +
        ", permissionId=" + permissionId +
        ", parentId=" + parentId +
        ", name=" + name +
        ", icon=" + icon +
        ", permissionName=" + permissionName +
        ", permissionUrl=" + permissionUrl +
        ", orderCount=" + orderCount +
        ", isUse=" + isUse +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
