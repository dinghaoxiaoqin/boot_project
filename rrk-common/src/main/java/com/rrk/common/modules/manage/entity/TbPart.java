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
public class TbPart extends Model<TbPart> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 角色id
     */
    @TableId(value = "part_id", type = IdType.AUTO)
    private Long partId;

    /**
     * 角色名称
     */
    private String partName;

    /**
     * 角色排序
     */
    private Integer partSort;

    /**
     * 是否可用（0可用，1不可用）
     */
    private Integer isUse;

    /**
     * 角色标记
     */
    private String partCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    public Long getPartId() {
        return partId;
    }

    public TbPart setPartId(Long partId) {
        this.partId = partId;
        return this;
    }

    public String getPartName() {
        return partName;
    }

    public TbPart setPartName(String partName) {
        this.partName = partName;
        return this;
    }

    public Integer getPartSort() {
        return partSort;
    }

    public TbPart setPartSort(Integer partSort) {
        this.partSort = partSort;
        return this;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public TbPart setIsUse(Integer isUse) {
        this.isUse = isUse;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbPart setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbPart setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.partId;
    }

    @Override
    public String toString() {
        return "TbPart{" +
        "partId=" + partId +
        ", partName=" + partName +
        ", partCode=" + partCode +
        ", partSort=" + partSort +
        ", isUse=" + isUse +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
