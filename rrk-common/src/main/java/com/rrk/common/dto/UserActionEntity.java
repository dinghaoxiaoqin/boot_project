package com.rrk.common.dto;

import com.rrk.common.constant.UserActionEnum;
import com.rrk.common.modules.product.dto.webdto.ProductDetailDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户行为分析的实体
 */
@Data
public class UserActionEntity implements Serializable {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 操作类型(0用户访问商品详情，1用户购买商品，2商品加入购物车)
     */
    private Integer operateType;
    /**
     * 用户操作的时间
     */
    private Date createTime;

    public UserActionEntity() {

    }

    /**
     * 用户访问商品详情
     *
     * @param userId
     * @param skuId
     * @param productDetailDto
     */
    public UserActionEntity(Long userId, Long skuId, ProductDetailDto productDetailDto) {
        this.createTime = new Date();
        this.operateType = UserActionEnum.USER_TO_PRODUCT.getActionType();
        this.productName = productDetailDto.getSkuName();
        this.userId = userId;
    }

    public UserActionEntity(Long userId, String productName) {
        this.createTime = new Date();
        this.operateType = UserActionEnum.USER_TO_ORDER.getActionType();
        this.productName = productName;
        this.userId = userId;
    }
}
