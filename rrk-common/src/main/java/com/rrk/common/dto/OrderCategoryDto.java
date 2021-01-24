package com.rrk.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单分类统计的结果
 */
@Data
public class OrderCategoryDto implements Serializable {

    /**
     * 省份名称
     */
    private String province;
    /**
     * 已支付的订单量
     */
    private Integer orderPayCount;

    /**
     * 已发货的订单量
     */
    private Integer orderDeliverCount;
    /**
     * 已完成订单量
     */
    private Integer orderFinishCount;
}
