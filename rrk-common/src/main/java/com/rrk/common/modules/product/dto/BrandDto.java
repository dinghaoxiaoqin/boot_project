package com.rrk.common.modules.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BrandDto implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 品牌名称
     */
    private String name;
    /**
     * 品牌首字母
     */
    private String letter;

    /**
     * 品牌图片
     */
    private String image;
    /**
     * 商品分类顶级id
     */
    private Long categoryId;
    /**
     * 次级商品分类id
     */
    private List<Long> pidIds;

    /**
     * 底层商品分类id
     */
    private List<Long> parIds;

}
