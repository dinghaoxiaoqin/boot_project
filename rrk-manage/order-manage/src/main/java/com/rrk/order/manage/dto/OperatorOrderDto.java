package com.rrk.order.manage.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单操作日志数据
 * @author dh
 * @date 2020-11-14
 */
@Data
public class OperatorOrderDto implements Serializable {

    /**
     * 操作人
     *
     */
    private String operatorBy;

    /**
     * 操作时间
     */
    private Date createTime;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 备注
     */
    private String remarks;
}
