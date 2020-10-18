package com.rrk.common.modules.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.order.dto.OrderInfo;
import com.rrk.common.modules.order.entity.TbOrder;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 * 订单主表 Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
public interface TbOrderMapper extends BaseMapper<TbOrder> {

    /**
     * 获取订单列表
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<OrderInfo> getOrderInfoList(@Param("userId") Long userId, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
}
