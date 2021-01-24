package com.rrk.order.manage.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 总平台的物流数据
 */
@Data
@ApiModel("总平台的物流数据")
public class LogisticsDto {

    /**
     * 运单号码
     */
    private String LogisticsCode;
    /**
     * 物流公司
     */
    private String ShipperCode;



    /**
     * 物流进度
     */
    private String State;
    /**
     * 是否成功
     */
    private String Success;

    private String EBusinessID;
    /**
     * 物流数据
     */
    private List<TraceDto> dataList;

}
