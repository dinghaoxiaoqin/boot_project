package com.rrk.common.modules.order.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 */
@Data
@Getter
@Setter
public class KillOrderDto implements Serializable {

    private Long skuId;

    private Long userId;
}
