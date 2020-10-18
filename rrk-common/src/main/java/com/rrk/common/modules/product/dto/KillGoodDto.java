package com.rrk.common.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * pc端秒杀商品实体
 */
@Data
@Getter
@Setter
public class KillGoodDto implements Serializable {

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productUrl;

    /**
     * 商品原售价
     */
    private BigDecimal productPrice;
    /**
     * 商品秒杀价格
     */
    private BigDecimal killProductPrice;
    /**
     * 活动状态（0 未开始 1秒杀中 2 已结束）
     */
    private Integer killStatus;

    private Integer killStock;

    /**
     * 活动创建时间
     */
    /**
     * 活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public KillGoodDto(){

    }

    public KillGoodDto(KillProductDto killProductDto) {
        this.setCreateTime(killProductDto.getStartTime());
        this.setProductId(killProductDto.getProductId());
        this.setProductName(killProductDto.getProductName());
        this.setProductPrice(killProductDto.getProductPrice());
        this.setProductUrl(killProductDto.getProductUrl());
        this.setKillProductPrice(killProductDto.getProductKillPrice());
        this.setKillStock(killProductDto.getProductKillStock());
        if (killProductDto.getStartTime().getTime() > System.currentTimeMillis()) {
            //未开始
            this.setKillStatus(0);
        } else if(killProductDto.getEndTime().getTime() <= System.currentTimeMillis()){
            //已结束
            this.setKillStatus(2);
        } else {
            this.setKillStatus(1);
        }
    }
}
