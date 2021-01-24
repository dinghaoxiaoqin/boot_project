package com.rrk.order.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单对应的快递轨迹信息
 */
@Data
@ApiModel(value = "快递轨迹信息")
public class TraceDto implements Serializable {

    /**
     * 快递的轨迹
     */
    @ApiModelProperty("快递的轨迹")
    private String AcceptStation;

    /**
     * 轨迹对应的时间
     */
    @ApiModelProperty("轨迹对应的时间")
   private String AcceptTime;
}
