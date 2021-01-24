package com.rrk.order.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 对应即时查询的实体
 */

@Data
@ApiModel(value = "跟踪物流信息")
public class KdNiaoDto implements Serializable {



    /**
     * 快递单号
     */
    @ApiModelProperty("快递单号")
    private String LogisticCode;

    /**
     * 物流状态（0：无轨迹 ，1已揽收 2在途中 3签收 4问题件）
     */
    @ApiModelProperty("物流状态（0：无轨迹 ，1已揽收 2在途中 3签收 4问题件）")
    private String State;

    /**
     * 公司快递鸟账号
     */
    @ApiModelProperty("快递鸟账号")
    private String EBusinessID;

    /**
     * 返回状态
     */
    @ApiModelProperty("返回状态")
    private String Success;

    /**
     * 快递的轨迹数据
     */
    @ApiModelProperty("快递的轨迹数据")
    private List<TraceDto> Traces;

    /**
     * 原因
     */
    @ApiModelProperty("失败的原因")
    private String Reason;



}
