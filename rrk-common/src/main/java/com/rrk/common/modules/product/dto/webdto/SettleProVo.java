package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SettleProVo implements Serializable {

    private Long addressId;


    private List<SettleVo> settleVos;
}
