package com.rrk.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 不同省份下的spu销售统计
 */
@Data
public class OrderProvinceDto implements Serializable {

    private String province;

    private List<OrderSpuDto> orderSpuDtos;
}
