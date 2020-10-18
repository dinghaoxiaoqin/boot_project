package com.rrk.common.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rrk.common.modules.order.dto.OrderDto;
import com.rrk.common.modules.product.entity.TbSku;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单商品表
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
@TableName("order_sku")
public class OrderSku extends Model<OrderSku> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 订单id
     */
    private String orderNo;

    /**
     * 商品skuId
     */
    private Long skuId;

    /**
     * 商品图片
     */
    private String skuImage;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 商品描述
     */
    private String skuDesc;

    /**
     * 购买sku的数量
     */
    private Integer skuNum;

    /**
     * 原价
     */
    private BigDecimal price;

    /**
     * 打折（几折 如 8折）
     */
    private Double beatPrice;

    /**
     * 售价
     */
    private BigDecimal salePrice;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public OrderSku() {
    }

    public OrderSku(Long userId, TbSku sku, OrderDto orderDto) {
        this.skuId = sku.getId();
        this.beatPrice = 0D;
        this.salePrice = sku.getSalePrice();
        this.price = sku.getPrice();
        this.createTime = new Date();
        this.orderNo = orderDto.getOrderNo();
        this.skuDesc = sku.getTitle();
        this.skuName = sku.getTitle();
        this.skuImage = sku.getImages().split(",")[0];
        this.skuNum = orderDto.getNum();
    }

    public Long getId() {
        return id;
    }

    public OrderSku setId(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public OrderSku setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public Long getSkuId() {
        return skuId;
    }

    public OrderSku setSkuId(Long skuId) {
        this.skuId = skuId;
        return this;
    }

    public String getSkuImage() {
        return skuImage;
    }

    public OrderSku setSkuImage(String skuImage) {
        this.skuImage = skuImage;
        return this;
    }

    public String getSkuName() {
        return skuName;
    }

    public OrderSku setSkuName(String skuName) {
        this.skuName = skuName;
        return this;
    }

    public String getSkuDesc() {
        return skuDesc;
    }

    public OrderSku setSkuDesc(String skuDesc) {
        this.skuDesc = skuDesc;
        return this;
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public OrderSku setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderSku setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Double getBeatPrice() {
        return beatPrice;
    }

    public OrderSku setBeatPrice(Double beatPrice) {
        this.beatPrice = beatPrice;
        return this;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public OrderSku setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public OrderSku setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public OrderSku setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrderSku{" +
        "id=" + id +
        ", orderNo=" + orderNo +
        ", skuId=" + skuId +
        ", skuImage=" + skuImage +
        ", skuName=" + skuName +
        ", skuDesc=" + skuDesc +
        ", skuNum=" + skuNum +
        ", price=" + price +
        ", beatPrice=" + beatPrice +
        ", salePrice=" + salePrice +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
