package com.rrk.common.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dinghao
 * @since 2020-09-14
 */
@TableName("tb_pay_log")
public class TbPayLog extends Model<TbPayLog> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 支付编号
     */
    private String payId;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付时间
     */
    private Date payTime;


    public Long getId() {
        return id;
    }

    public TbPayLog setId(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public TbPayLog setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public String getPayId() {
        return payId;
    }

    public TbPayLog setPayId(String payId) {
        this.payId = payId;
        return this;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public TbPayLog setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
        return this;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbPayLog{" +
        "id=" + id +
        ", orderNo=" + orderNo +
        ", payId=" + payId +
        ", payAmount=" + payAmount +
        ", payTime=" + payTime +
        "}";
    }
}
