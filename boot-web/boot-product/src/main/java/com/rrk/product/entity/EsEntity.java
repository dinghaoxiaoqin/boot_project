package com.rrk.product.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 普通数据分析的实体
 */
@Data
public class EsEntity implements Serializable {

    private Long id;

    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 商品spuId;
     */
    private Long spuId;
    /**
     * 商品分类1
     */
    private Long cid1;
    /**
     * 商品分类2
     */
    private Long cid2;
    /**
     * 商品分类3
     */
    private Long cid3;
    /**
     *商品spu名称
     */
    private String spuName;
    /**
     * 上下架状态
     */
    private Integer saleable;
    /**
     * 是否删除
     */
    private Integer valid;
    /**
     * 商品spu描述
     */
    private String spuDescrition;
    /**
     * 商品skuId
     */
    private Long skuId;
    /**
     * 商品的第一个图片
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
     * 商品库存
     */
    private Integer stock;
    /**
     * 商品销量
     */
    private Integer saleCount;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 建议搜索词
     */
    private String productSuggester;

    /**
     * 高亮的字段
     */
    private String highLight;
}
