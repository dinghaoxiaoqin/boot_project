package com.rrk.common.modules.order.dto;

import com.rrk.common.modules.order.entity.OrderSku;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品
 */
@Data
public class OrderSkuDto implements Serializable {

    /**
     * skuId
     */
    private Long skuId;
    /**
     * 图片
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
     * 商品售价
     */
    private BigDecimal salePrice;
    public OrderSkuDto() {
    }

    public OrderSkuDto(OrderSku orderSku) {
        this.skuId = orderSku.getSkuId();
        this.salePrice = orderSku.getSalePrice();
        this.skuImage = orderSku.getSkuImage();
        this.skuDesc = orderSku.getSkuDesc();
        this.skuName = orderSku.getSkuName();

    }
}
