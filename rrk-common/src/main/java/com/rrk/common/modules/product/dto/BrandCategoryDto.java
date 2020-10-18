package com.rrk.common.modules.product.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandCategoryDto implements Serializable {

    /**
     * 品牌id
     */
    private Long id;
    /**
     * 品牌名称
     */
    private String name;
    /**
     * 分类编号
     */
    private Long categoryId;
    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 品牌头像
     */
    private String image;
    /**
     * 品牌首字母
     */
    private String letter;



}
