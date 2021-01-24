package com.rrk.common.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单快递信息
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
@TableName("order_fast")
public class OrderFast extends Model<OrderFast> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 物流公司编码
     */
    private String logisticCode;

    /**
     * 物流公司名称
     */
    private String logisticName;

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

    public OrderFast setId(Long id) {
        this.id = id;
        return this;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public OrderFast setExpressNo(String expressNo) {
        this.expressNo = expressNo;
        return this;
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public OrderFast setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
        return this;
    }

    public String getLogisticName() {
        return logisticName;
    }

    public OrderFast setLogisticName(String logisticName) {
        this.logisticName = logisticName;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public OrderFast setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public OrderFast setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public OrderFast setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrderFast{" +
        "id=" + id +
        ", orderNo=" + orderNo +
        ", expressNo=" + expressNo +
        ", logisticCode=" + logisticCode +
        ", logisticName=" + logisticName +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
