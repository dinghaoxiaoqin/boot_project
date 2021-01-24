package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class ActivityDto implements Serializable {

    private Long skuId;

    private Long activityId;

    private Integer nums;

}
