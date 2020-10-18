package com.rrk.common.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author dinghao
 * @since 2020-02-08
 */
@TableName("tb_role")
public class TbRole extends Model<TbRole> implements Serializable{

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称(汉字)
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 角色的英文code.如：ADMIN
     */
    private String roleCode;

    /**
     * 角色顺序
     */
    private Integer sort;

    /**
     * 0表示可用
     */
    private Integer status;

    /**
     * 角色的创建日期
     */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public TbRole setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public TbRole setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public TbRole setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
        return this;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public TbRole setRoleCode(String roleCode) {
        this.roleCode = roleCode;
        return this;
    }

    public Integer getSort() {
        return sort;
    }

    public TbRole setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public TbRole setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbRole setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbRole{" +
        "id=" + id +
        ", roleName=" + roleName +
        ", roleDesc=" + roleDesc +
        ", roleCode=" + roleCode +
        ", sort=" + sort +
        ", status=" + status +
        ", createTime=" + createTime +
        "}";
    }
}
