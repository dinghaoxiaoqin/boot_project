package com.rrk.order.manage.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.rrk.common.constant.NumConstants;
import com.rrk.common.modules.order.entity.OrderFast;
import com.rrk.common.modules.order.entity.OrderSku;
import com.rrk.common.modules.order.entity.TbOperatorLog;
import com.rrk.common.modules.order.entity.TbOrder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 获取订单详情实体
 * @author dh
 * @date 2020-11-14
 */
@Data
public class OrderDetailDto implements Serializable {

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 订单支付超时时间
     */
    private Date overTime;
    /**
     * 订单支付时间
     */
    private Date payTime;

    /**
     * 订单发货时间
     */
    private Date sentTime;
    /**
     * 订单完成时间
     */
    private Date finishTime;
    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户账户
     */
    private Long userId;
    /**
     * 支付方式
     */
    private Integer payType;
    /**
     * 快递公司
     */
    private String logisticName;
    /**
     * 物流单号
     */
    private String expressNo;

    /**
     * 地址id
     */
    private Long addressId;
    /**
     * 自动确认超时时间
     */
    private Integer autoConfirmDay;
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
     * 实收总价
     */
    private BigDecimal totalAmount;

    private List<OrderSkuDto> orderSkus;

    private List<OperatorOrderDto> operatorOrderDtos;
    public OrderDetailDto() {
    }

    public OrderDetailDto(TbOrder one, OrderFast orderFast, List<OrderSku> list, List<TbOperatorLog> logs) {
        this.createTime = one.getCreateTime();
        this.overTime = one.getOverTime();
        this.sentTime = one.getSendTime();
        this.finishTime = one.getFinishTime();
        this.payTime = one.getPayTime();
        this.orderNo = one.getOrderNo();
        this.orderStatus = one.getOrderStatus();
        this.userId = one.getUserId();
        this.payType = one.getPayType();
        if (ObjectUtil.isNotNull(orderFast)) {
            this.logisticName = orderFast.getLogisticName();
            this.expressNo = orderFast.getExpressNo();
        }
        this.autoConfirmDay = NumConstants.OVER_CONFIRM_ORDER;
        this.addressId = one.getAddressId();
        this.totalAmount = one.getAmount();
        List<OrderSkuDto> orderSkus = new ArrayList<>();
        for (OrderSku orderSku : list) {
            OrderSkuDto orderSkuDto = new OrderSkuDto();
            orderSkuDto.setSalePrice(orderSku.getSalePrice());
            orderSkuDto.setSkuDesc(orderSku.getSkuDesc());
            orderSkuDto.setSkuId(orderSku.getSkuId());
            orderSkuDto.setSkuImage(orderSku.getSkuImage());
            orderSkuDto.setSkuName(orderSku.getSkuName());
            orderSkuDto.setSkuNum(orderSku.getSkuNum());
            orderSkus.add(orderSkuDto);
        }
        this.orderSkus = orderSkus;
      List<OperatorOrderDto> operatorOrderDtos = new ArrayList<>();
        if (CollUtil.isNotEmpty(logs)) {
            for (TbOperatorLog log : logs) {
                OperatorOrderDto operatorOrderDto = new OperatorOrderDto();
                operatorOrderDto.setCreateTime(log.getCreateTime());
                operatorOrderDto.setOperatorBy(log.getOperatorBy());
                operatorOrderDto.setOrderStatus(log.getOrderStatus());
                operatorOrderDto.setRemarks(log.getRemarks());
                operatorOrderDtos.add(operatorOrderDto);
            }
        }

        this.operatorOrderDtos = operatorOrderDtos;
    }
}
