package com.rrk.common.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统组织结构表
 * </p>
 *
 * @author dinghao
 * @since 2020-02-08
 */
@TableName("tb_org")
public class TbOrg extends Model<TbOrg> implements Serializable{

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 上级组织编码
     */
    private Integer orgPid;

    /**
     * 所有的父节点id
     */
    private String orgPids;

    /**
     * 0:不是叶子节点，1:是叶子节点
     */
    private Integer isLeaf;

    /**
     * 组织名
     */
    private String orgName;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮件
     */
    private String email;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 组织层级
     */
    private Integer level;

    /**
     * 0:启用,1:禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public TbOrg setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getOrgPid() {
        return orgPid;
    }

    public TbOrg setOrgPid(Integer orgPid) {
        this.orgPid = orgPid;
        return this;
    }

    public String getOrgPids() {
        return orgPids;
    }

    public TbOrg setOrgPids(String orgPids) {
        this.orgPids = orgPids;
        return this;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public TbOrg setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
        return this;
    }

    public String getOrgName() {
        return orgName;
    }

    public TbOrg setOrgName(String orgName) {
        this.orgName = orgName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public TbOrg setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public TbOrg setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TbOrg setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getSort() {
        return sort;
    }

    public TbOrg setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public TbOrg setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public TbOrg setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbOrg setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbOrg setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbOrg{" +
        "id=" + id +
        ", orgPid=" + orgPid +
        ", orgPids=" + orgPids +
        ", isLeaf=" + isLeaf +
        ", orgName=" + orgName +
        ", address=" + address +
        ", phone=" + phone +
        ", email=" + email +
        ", sort=" + sort +
        ", level=" + level +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
