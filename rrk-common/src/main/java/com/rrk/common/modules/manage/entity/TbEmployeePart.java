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
public class TbEmployeePart extends Model<TbEmployeePart> implements Serializable {

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
     * 角色id
     */
    private Long partId;

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

    public TbEmployeePart setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public TbEmployeePart setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public Long getPartId() {
        return partId;
    }

    public TbEmployeePart setPartId(Long partId) {
        this.partId = partId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbEmployeePart setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbEmployeePart setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbEmployeePart{" +
        "id=" + id +
        ", employeeId=" + employeeId +
        ", partId=" + partId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
