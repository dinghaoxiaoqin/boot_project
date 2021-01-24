package com.rrk.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderProductDto implements Serializable {

    private Long userId;

    private Long skuId;

    private String skuName;

    private String spuName;

    private String receiverCity;

    private String productCategory;

    private String productBrand;

    private BigDecimal salePrice;

    private Integer nums;
}
