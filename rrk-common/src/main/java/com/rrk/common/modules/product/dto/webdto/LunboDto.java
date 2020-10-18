package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;

/**
 * 移动端首页轮播图的商品数据
 */
@Data
public class LunboDto implements Serializable {

    /**
     * 商品skuId
     */
    private Long skuId;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 商品图片
     */
    private String skuImage;
}
