package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端传入结算参数 实体
 */
@Data
public class SettleVo implements Serializable {

    private Long shopId;

    private Long skuId;


}
