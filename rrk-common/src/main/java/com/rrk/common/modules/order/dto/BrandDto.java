package com.rrk.common.modules.order.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class BrandDto implements Serializable {

    private String brandName;

    private Integer brandCount;
}
