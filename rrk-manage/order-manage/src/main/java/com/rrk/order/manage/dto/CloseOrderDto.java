package com.rrk.order.manage.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2020-11-15
 * @author dh
 * 关闭订单实体
 */
@Data
public class CloseOrderDto implements Serializable {

    /**
     * 是否添加备注
     */
    private Boolean dialogVisible;

    /**
     * 订单备注的内容
     */
    private String content;

    /**
     * 订单id
     */
    private Long orderId;
}
