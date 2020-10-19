package com.rrk.product.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 热搜的实体
 */
@Data
public class HotWordEntity implements Serializable {

    /**
     * 商品skuId
     */
    private Long skuId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品第一张图片
     */
    private String skuImage;
    /**
     * 商品原价
     */
    private BigDecimal price;
    /**
     * 商品售价
     */
    private BigDecimal salePrice;
    /**
     * 商品销量
     */
    private Integer saleCount;
    /**
     * 搜索量
     */
    private Long queryCount;
    /**
     * 时间（搜索的时间）
     */
    private Date createTime;

    public HotWordEntity() {
    }

    public HotWordEntity(EsEntity esEntity) {
        this.skuImage = esEntity.getSkuImage().split(",")[0];
        this.saleCount = esEntity.getSaleCount();
        this.createTime = esEntity.getCreateTime();
        this.skuId = esEntity.getSkuId();
        this.price = esEntity.getPrice();
        this.productName = esEntity.getSpuName();
        this.salePrice = esEntity.getSalePrice();
    }
}
