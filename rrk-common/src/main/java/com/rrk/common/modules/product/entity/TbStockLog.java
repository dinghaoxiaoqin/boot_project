package com.rrk.common.modules.product.entity;

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
 * @since 2020-09-09
 */
@TableName("tb_stock_log")
public class TbStockLog extends Model<TbStockLog> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作员id
     */
    private Long employeeId;

    /**
     * 规格id
     */
    private Long skuId;

    /**
     * 增加的库存数量
     */
    private Integer stock;

    /**
     * 操作类型（）
     */
    private String operateType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remarks;


    public Long getId() {
        return id;
    }

    public TbStockLog setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public TbStockLog setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public Long getSkuId() {
        return skuId;
    }

    public TbStockLog setSkuId(Long skuId) {
        this.skuId = skuId;
        return this;
    }

    public Integer getStock() {
        return stock;
    }

    public TbStockLog setStock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public String getOperateType() {
        return operateType;
    }

    public TbStockLog setOperateType(String operateType) {
        this.operateType = operateType;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbStockLog setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    public TbStockLog setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbStockLog{" +
        "id=" + id +
        ", employeeId=" + employeeId +
        ", skuId=" + skuId +
        ", stock=" + stock +
        ", operateType=" + operateType +
        ", createTime=" + createTime +
        ", remarks=" + remarks +
        "}";
    }
}
