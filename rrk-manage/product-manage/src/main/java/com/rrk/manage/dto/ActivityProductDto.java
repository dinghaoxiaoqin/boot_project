package com.rrk.manage.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ActivityProductDto implements Serializable {


    private Long id;

    private Long activityId;

    private Long skuId;

    private Integer isSell;

    private BigDecimal activityPrice;

    private Integer activityStock;

    private Date startTime;

    private Date endTime;


}
