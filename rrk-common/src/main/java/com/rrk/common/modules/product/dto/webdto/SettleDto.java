package com.rrk.common.modules.product.dto.webdto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 结算列表
 */
@Data
public class SettleDto implements Serializable {

    /**
     *
     */
    private Long userId;

    /**
     * 收货人手机号
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货人姓名
     */
    private String deliverName;


    /**
     * 筹单满减优惠
     */
    private BigDecimal giftPrefe;

    /**
     * （打折）优惠
     */
    private BigDecimal cOrderAmount;

    /**
     * 实付金额
     */
    private BigDecimal sumAmount;

    private List<ProductDto> productDtos;




}
