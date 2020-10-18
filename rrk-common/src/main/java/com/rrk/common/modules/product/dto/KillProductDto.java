package com.rrk.common.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 参与秒杀商品的实体
 */
@Data
@Setter
@Getter
public class KillProductDto implements Serializable {

    /**
     * 参与秒杀商品id
     */
    private Long productId;
    /**
     * 秒杀商品名称
     */
    private String productName;

    /**
     * 秒杀商品库存
     */
    private Integer productKillStock;

    /**
     * 商品售价
     */
    private BigDecimal productPrice;

    /**
     * 商品图片
     */
    private String productUrl;

    /**
     * 秒杀价
     */
    private BigDecimal productKillPrice;
    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
