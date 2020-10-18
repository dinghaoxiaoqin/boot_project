package com.rrk.common.modules.order.dto;

import com.rrk.common.constant.PayContants;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付宝数据封装
 */
public class AliNotifyResDto implements Serializable {
    private String tradeId;
    private String subject;
    @ApiModelProperty("支付业务处理编码")
    private String subCode;
    private String buyerLogonId;

    @ApiModelProperty("定单类型")
    private Integer orderType;
    @ApiModelProperty("定单编号")
    private String orderId;
    @ApiModelProperty("支付交易号,由支付平台返回")
    private String payOrderId;
    @ApiModelProperty("付款时间")
    private Date payTime;
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    private String data;

    public Integer getPayType() {
        return PayContants.PAY_TYPE_ALI;
    }

    public AliNotifyResDto() {
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
