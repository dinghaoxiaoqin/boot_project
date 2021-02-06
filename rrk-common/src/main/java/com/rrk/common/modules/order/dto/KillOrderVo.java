package com.rrk.common.modules.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class KillOrderVo implements Serializable {

    /**
     * 未支付订单
     */
    public static final Integer NO_PAY_ORDER = 0;
    /**
     * 已经支付
     */
    public static final Integer PAY_ORDER = 1;

    private String orderNo;

    private Long userId;

    private Date createTime;

    private Long skuId;

    private BigDecimal amount;

    private Date overTime;
}
