package com.rrk.common.modules.order.entity;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rrk.common.modules.order.dto.GeneralOrderDto;
import com.rrk.common.modules.product.entity.TbProduct;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dinghao
 * @since 2020-04-15
 */
public class TbGeneralOrder extends Model<TbGeneralOrder> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 普通订单id
     */
    private String orderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品图片
     */
    private String productUrl;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 订单状态（0未支付 1已支付）
     */
    private Integer orderStatus;

    /**
     * 实收金额
     */
    private BigDecimal orderAmount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public TbGeneralOrder(){}

    public TbGeneralOrder(GeneralOrderDto generalOrderDto, TbProduct product,Long userId) {
        this.setBuyNum(generalOrderDto.getNum());
        this.setCreateTime(new Date());
        this.setOrderAmount(NumberUtil.mul(product.getProductPrice(),generalOrderDto.getNum()));
        this.setUserId(userId);
        this.setProductName(product.getProductName());
        this.setProductUrl(product.getProductUrl());
        this.setOrderId(IdWorker.getIdStr());
        this.setOrderStatus(0);
        this.setUserName("张三");
        //this.setProductId(generalOrderDto.getProductId());
    }

    public Long getId() {
        return id;
    }

    public TbGeneralOrder setId(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public TbGeneralOrder setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public TbGeneralOrder setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public TbGeneralOrder setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public TbGeneralOrder setProductUrl(String productUrl) {
        this.productUrl = productUrl;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public TbGeneralOrder setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public TbGeneralOrder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public TbGeneralOrder setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
        return this;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public TbGeneralOrder setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public TbGeneralOrder setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbGeneralOrder setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbGeneralOrder setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbGeneralOrder{" +
        "id=" + id +
        ", orderId=" + orderId +
        ", userId=" + userId +
        ", productId=" + productId +
        ", productUrl=" + productUrl +
        ", productName=" + productName +
        ", userName=" + userName +
        ", buyNum=" + buyNum +
        ", orderStatus=" + orderStatus +
        ", orderAmount=" + orderAmount +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
