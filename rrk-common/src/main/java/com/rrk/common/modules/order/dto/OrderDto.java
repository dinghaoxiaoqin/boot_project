package com.rrk.common.modules.order.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDto implements Serializable {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 购买的skuId
     */
    private Long skuId;
    /**
     * 用户收货地址id
     */
    private Long addressId;
    /**
     * 用户购买sku的数量
     */
    private Integer num;
}
