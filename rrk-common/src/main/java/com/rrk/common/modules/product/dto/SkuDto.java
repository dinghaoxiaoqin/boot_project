package com.rrk.common.modules.product.dto;

import com.rrk.common.modules.product.entity.TbSku;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Data
public class SkuDto implements Serializable {

    /**
     * sku的id
     */
    private Long id;
    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品图片
     */
    private List<String> images;

    /**
     * 商品原价
     */
    private BigDecimal price;

    /**
     * 商品售价
     */
    private BigDecimal salePrice;
    /**
     * 商品是否打折（0否 1是）
     */
    private Integer isVip;

    /**
     * 规格属性
     */
    private String ownSpec;

    /**
     * 商品库存
     */
    private Integer stock;
    /**
     * 商品销量
     */
    private Integer saleCount;

    /**
     * 是否有效（0无效，1有效）
     */
    private Integer enable;

    public SkuDto() {
    }

    public SkuDto(TbSku record) {
        this.id = record.getId();
        this.enable = record.getEnable();
        this.isVip = record.getIsVip();
        this.ownSpec = record.getOwnSpec();
        this.price = record.getPrice();
        this.salePrice = record.getSalePrice();
        this.saleCount = record.getSaleCount();
        this.stock = record.getStock();
        this.title = record.getTitle();
        String[] split = record.getImages().split(",");
        List<String> list = Arrays.asList(split);
        this.images = list;
    }
}
