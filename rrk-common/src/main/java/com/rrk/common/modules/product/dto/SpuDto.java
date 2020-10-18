package com.rrk.common.modules.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品spu的相关信息
 */
@Data
public class SpuDto implements Serializable {
    /**
     * 编号
     */
    private Long id;

    /**
     * spu主标题
     */
    private String title;
    /**
     * spu副标题
     */
    private String subTitle;
    /**
     * 1级类目id
     */
    private Long cid1;
    /**
     * 2级类目id
     */
    private Long cid2;

    /**
     * 3级类目id
     */
    private Long cid3;

    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 上下架状态(0下架 1上架)
     */
    private Integer saleable;
    /**
     * 是否有效(0无效 ，1 有效)
     */
    private Integer  valid;

    /**
     * 商品spu描述
     */
    private String spuDescrition;

}
