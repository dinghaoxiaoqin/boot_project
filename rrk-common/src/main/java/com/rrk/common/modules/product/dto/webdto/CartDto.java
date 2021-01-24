package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车商品entity
 */
@Data
public class CartDto implements Serializable {

    /**
     * 商品skuId
     */
    private Long skuId;

    /**
     * 商品spuId
     */
    private Long spuId;
    /**
     * 商品sku名称
     */
    private String skuName;

    /**
     * 商品sku描述
     */
    private String skuDesc;
    /**
     * 图片
     */
    private String skuImage;
    /**
     * 售价
     */
    private BigDecimal salePrice;
    /**
     * 数量
     */
    private Integer nums;

    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 店铺名称
     */
    private String shopName;
}
