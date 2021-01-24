package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 结算列表商品实体
 */
@Data
public class ProductDto implements Serializable {

    /**
     * skuId
     */
    private Long skuId;

    /**
     * spuId
     */
    private Long spuId;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * sku图片
     */
    private String imageUrl;

    /**
     * 售价
     */
    private BigDecimal salesPrice;

    /**
     * 数量
     */
    private Integer nums;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 优惠金额
     */
    private BigDecimal proPrefe;

    private Long activityId;

    private BigDecimal manAmount;

    private BigDecimal giftAmount;
    private Integer isGift;

    private Integer isActivity;

    private Integer isSell;









}
