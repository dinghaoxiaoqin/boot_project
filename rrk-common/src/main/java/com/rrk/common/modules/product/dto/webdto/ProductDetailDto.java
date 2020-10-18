package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 移动端商品详情
 */
@Data
public class ProductDetailDto implements Serializable {
    /**
     * skuId
     */
    private Long skuId;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 商品子标题
     */
    private String subTitle;
    /**
     * 商品图片
     */
    private String images;
    /**
     * 商品原价
     */
    private BigDecimal price;
    /**
     * 商品售价
     */
    private BigDecimal salePrice;
    /**
     * 是否参与会员打折（0否，1是）
     */
    private Integer isVip;
    /**
     * 商品库存
     */
    private Integer stock;
    /**
     * 商品销量
     */
    private Integer saleCount;
    /**
     * sku的特有规格参数键值对
     */
    private String ownSpec;
    /**
     * 是否有效，0无效，1有效
     */
    private Integer enable;
    /**
     * 商品描述
     */
    private String spuDescrition;
    /**
     * 时间
     */
    private Date createTime;
}
