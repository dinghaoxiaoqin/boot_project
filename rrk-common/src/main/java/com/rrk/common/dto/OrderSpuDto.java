package com.rrk.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderSpuDto implements Serializable {

    private String spuName;

    private Integer nums;

    private BigDecimal amount;
}
