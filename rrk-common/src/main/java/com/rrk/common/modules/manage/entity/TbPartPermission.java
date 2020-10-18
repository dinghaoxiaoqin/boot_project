package com.rrk.common.modules.manage.entity;

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
 * @since 2020-04-25
 */
@TableName("tb_part_permission")
public class TbPartPermission extends Model<TbPartPermission> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 员工角色id
     */
    private Long partId;

    /**
     * 员工权限id
     */
    private Long permissionId;

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

    public TbPartPermission setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getPartId() {
        return partId;
    }

    public TbPartPermission setPartId(Long partId) {
        this.partId = partId;
        return this;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public TbPartPermission setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbPartPermission setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbPartPermission setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbPartPermission{" +
        "id=" + id +
        ", partId=" + partId +
        ", permissionId=" + permissionId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
