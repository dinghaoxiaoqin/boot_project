package com.rrk.common.modules.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderBrandStatisticDto implements Serializable {

    private String brand;

    private Integer orderCount;

    private Integer nums;

    private BigDecimal amount;
}
