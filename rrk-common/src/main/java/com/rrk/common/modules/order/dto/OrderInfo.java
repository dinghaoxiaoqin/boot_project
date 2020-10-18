package com.rrk.common.modules.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderInfo implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 总价
     */
    private BigDecimal amount;
    /**
     * 购买总数
     */
    private Integer buyNum;

    private List<OrderSkuInfo> orderSkuInfos;

}
