package com.rrk.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.order.dto.KillOrderVo;
import com.rrk.common.modules.order.entity.OrderSku;

/**
 * <p>
 * 订单商品表 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
public interface IOrderSkuService extends IService<OrderSku> {

    /**
     * 秒杀订单
     * @param orderVo
     */
    void addKillOrder(Integer orderStatus,KillOrderVo orderVo);
}
