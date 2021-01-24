package com.rrk.order.manage.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单sku实体
 * @author dh
 * @date 2020-11-14
 */
@Data
public class OrderSkuDto implements Serializable {

    /**
     * 商品sku编号
     */
    private Long skuId;

    /**
     * 商品sku名称
     */
    private String skuName;
    /**
     * sku的images
     */
    private String skuImage;
    /**
     * sku的售价
     */
    private BigDecimal salePrice;

    /**
     * 商品sku详情
     */
    private String skuDesc;
    /**
     * 购买sku的数量
     */
    private Integer skuNum;



}
