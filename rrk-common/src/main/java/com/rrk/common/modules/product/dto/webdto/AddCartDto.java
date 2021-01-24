package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品添加到购物车的实体
 */
@Data
public class AddCartDto implements Serializable {

    /**
     * skuId
     */
    private Long skuId;
    /**
     * 商品spuId
     */
    private Long spuId;

    /**
     * 商品sku数量
     */
    private Integer nums;

    /**
     * 店铺id
     */
    private Long shopId = 0L;
}
