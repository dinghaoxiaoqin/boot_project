package com.rrk.common.modules.product.dto.webdto;

import com.rrk.common.modules.product.entity.TbSku;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 精品推荐商品的dto
 */
@Data
public class RecommendDto implements Serializable {
    /**
     * 商品skuId
     */
    private Long skuId;

    /**
     * 商品sku名称
     */
    private String skuName;
    /**
     * 商品sku的图片
     */
    private String skuImage;
    /**
     * 商品售价
     */
    private BigDecimal salePrice;
    /**
     * 商品销量
     */
    private Integer saleCount;

    public RecommendDto() {
    }

    public RecommendDto(TbSku record) {
        this.skuId = record.getId();
        this.skuName = record.getTitle();
        this.skuImage = record.getImages().split(",")[0];
        this.saleCount = record.getSaleCount();
        this.salePrice = record.getSalePrice();
    }
}
