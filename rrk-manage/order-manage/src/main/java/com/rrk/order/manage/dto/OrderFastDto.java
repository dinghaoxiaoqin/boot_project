package com.rrk.order.manage.dto;

import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.common.modules.user.entity.TbUserAddress;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单发货信息
 */
@Data
public class OrderFastDto implements Serializable {

    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 收货人id
     */
    private Long userId;

    /**
     * 收货人名称
     */
    private String receiverName;

    /**
     * 收货人手机号
     */
    private String receiverPhone;

    /**
     * 收货人详细地址
     */
    private String receiverAddress;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 物流公司编码
     */
    private String logisticCode;

    /**
     * 物流公司名称
     */
    private String logisticName;

    public OrderFastDto() {
    }

    public OrderFastDto(TbOrder order, TbUser user, TbUserAddress userAddress) {
        this.orderId = order.getId();
       this.orderNo = order.getOrderNo();
       this.receiverAddress = userAddress.getUserAddress();
       this.receiverName = user.getNickName();
       this.receiverPhone = user.getPhone();
       this.userId = user.getUserId();
    }
}
