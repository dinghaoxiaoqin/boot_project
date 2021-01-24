package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SkuDto implements Serializable {

    private Long skuId;

    private String skuImage;

    private String skuName;

    private String skuDesc;

    private BigDecimal price;

    private BigDecimal salePrice;

    private String spuName;

    private String productBrand;

    private String productCategory;
}
