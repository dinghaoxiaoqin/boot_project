package com.rrk.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.order.dto.AliNotifyResDto;
import com.rrk.common.modules.order.dto.GeneralOrderDto;
import com.rrk.common.modules.order.dto.OrderDto;
import com.rrk.common.modules.order.dto.OrderInfo;
import com.rrk.common.modules.order.entity.TbOrder;

/**
 * <p>
 * 订单主表 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
public interface ITbOrderService extends IService<TbOrder> {

    /**
     * 生成订单号
     * @param userId
     * @param
     * @return
     */
    String getOrderId(Long userId);
    /**
     * 用户下单
     * @param orderDto
     * @return
     */
    Integer addOrder(OrderDto orderDto,Long userId);

    /**
     * 支付回调修改订单状态
     * @param aliNotifyResDto
     * @return
     */
    Boolean updateOrderStaus(AliNotifyResDto aliNotifyResDto);

    /**
     * 获取订单详情信息
     * @param orderNo
     * @return
     */
    GeneralOrderDto getOrderByOrderNo(String orderNo);

    /**
     * 获取订单列表
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    IPage<OrderInfo> getOrderInfoList(Long userId, Integer pageNo, Integer pageSize);
}
