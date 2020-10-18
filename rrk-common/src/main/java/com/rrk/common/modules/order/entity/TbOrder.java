package com.rrk.common.modules.order.entity;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rrk.common.constant.OrderContants;
import com.rrk.common.modules.order.dto.OrderDto;
import com.rrk.common.modules.product.entity.TbSku;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单主表
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
@TableName("tb_order")
public class TbOrder extends Model<TbOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 下单客户id
     */
    private Long userId;

    /**
     * 购买商品skuId
     */
    private Long skuId;

    /**
     * 用户的发货地址id
     */
    private Long addressId;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 订单状态（0未支付，1代发货，2待收货，3已完成，4已取消）
     */
    private Integer orderStatus;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 实付金额
     */
    private BigDecimal amount;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 优惠券id
     */
    private Long couponNo;

    /**
     * 订单备注
     */
    private String remarks;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 订单支付超时时间
     */
    private Date overTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 发货时间
     */
    private Date sendTime;

    /**
     * 完成时间
     */
    private Date finishTime;
    public TbOrder() {
    }

    public TbOrder(Long userId, TbSku sku, OrderDto orderDto) {
        //普通订单超时 半小时未支付就自动取消
        DateTime dateTime = DateUtil.offsetMinute(new Date(), OrderContants.KILL_ORDER_OVER);
        this.overTime = dateTime;
        this.userId = userId;
        this.orderStatus = OrderContants.WAIT_ORDER_STATUS;
        this.createTime = new Date();
        this.addressId = orderDto.getAddressId();
        this.orderNo = orderDto.getOrderNo();
        this.buyNum = orderDto.getNum();
        this.amount = NumberUtil.mul(orderDto.getNum(),sku.getSalePrice());
        this.shopId = 0L;
        this.skuId = sku.getId();
    }

    public Long getId() {
        return id;
    }

    public TbOrder setId(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public TbOrder setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public TbOrder setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getSkuId() {
        return skuId;
    }

    public TbOrder setSkuId(Long skuId) {
        this.skuId = skuId;
        return this;
    }

    public Long getAddressId() {
        return addressId;
    }

    public TbOrder setAddressId(Long addressId) {
        this.addressId = addressId;
        return this;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public TbOrder setExpressNo(String expressNo) {
        this.expressNo = expressNo;
        return this;
    }

    public Long getShopId() {
        return shopId;
    }

    public TbOrder setShopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public TbOrder setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public Integer getPayType() {
        return payType;
    }

    public TbOrder setPayType(Integer payType) {
        this.payType = payType;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TbOrder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public TbOrder setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
        return this;
    }

    public Long getCouponNo() {
        return couponNo;
    }

    public TbOrder setCouponNo(Long couponNo) {
        this.couponNo = couponNo;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    public TbOrder setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbOrder setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getPayTime() {
        return payTime;
    }

    public TbOrder setPayTime(Date payTime) {
        this.payTime = payTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbOrder setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public TbOrder setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbOrder{" +
                "id=" + id +
                ", orderNo=" + orderNo +
                ", userId=" + userId +
                ", skuId=" + skuId +
                ", addressId=" + addressId +
                ", expressNo=" + expressNo +
                ", shopId=" + shopId +
                ", orderStatus=" + orderStatus +
                ", payType=" + payType +
                ", amount=" + amount +
                ", buyNum=" + buyNum +
                ", couponNo=" + couponNo +
                ", remarks=" + remarks +
                ", createTime=" + createTime +
                ", overTime=" + overTime +
                ", payTime=" + payTime +
                ", updateTime=" + updateTime +
                ", sendTime=" + sendTime +
                ", finishTime=" + finishTime +
                "}";
    }
}
