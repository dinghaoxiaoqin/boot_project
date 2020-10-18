package com.rrk.common.modules.order.dto;

import com.rrk.common.modules.order.entity.OrderSku;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.common.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class GeneralOrderDto implements Serializable {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单超时时间
     */
    private Long overTime;

    /**
     * 下单时间
     */
    private String createTime;

    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 收货人名称
     */
    private String userName;
    /**
     * 收货人手机号
     */
    private String userMobile;
    /**
     * 收货人地址
     */
    private String address;
    /**
     * 支付方式
     */
    private Integer payType;
    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 购买件数
     */
    private Integer num;

    /**
     * 订单商品信息表
     */
    private List<OrderSkuDto> orderSkuDtos;
    public GeneralOrderDto() {
    }

    public GeneralOrderDto(TbOrder order, List<OrderSku> orderSkus, TbUserAddress address) {
        this.userMobile = address.getUserMobile();
        this.address = address.getUserAddress();
        this.userName = address.getUserName();
        this.orderNo = order.getOrderNo();
        this.amount = order.getAmount();
        this.num = order.getBuyNum();
        this.createTime = DateUtils.formatDate(order.getCreateTime(),"yyyy-MM-dd HH:mm:ss");
        this.overTime = order.getOverTime().getTime();
        this.payType = order.getPayType();
        this.orderStatus = order.getOrderStatus();
        List<OrderSkuDto> list = new ArrayList<>();
        for (OrderSku orderSku : orderSkus) {
            OrderSkuDto orderSkuDto = new OrderSkuDto(orderSku);
            list.add(orderSkuDto);
        }
        this.orderSkuDtos = list;

    }
}
