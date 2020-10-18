package com.rrk.common.modules.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单列表实体
 */
@Data
public class OrderSkuInfo implements Serializable {

    /**
     * 商品skuId
     */
    private Long skuId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品规格
     */
    private String skuName;
    /**
     * 商品图片
     */
    private String skuImage;
    /**
     * 商品数量
     */
    private Integer buyNum;
    /**
     * 商品实付金额
     */
    private BigDecimal salePrice;

    /**
     *原价
     */
    private BigDecimal price;

}
