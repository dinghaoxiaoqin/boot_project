package com.rrk.common.modules.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.dto.OrderProductDto;
import com.rrk.common.modules.order.dto.OrderBrandStatisticDto;
import com.rrk.common.modules.order.dto.OrderInfo;
import com.rrk.common.modules.order.entity.TbOrder;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    List<OrderBrandStatisticDto> getStatisticByBrand(@Param("start") Date start, @Param("end") Date end, @Param("province") String province,@Param("category")String category);

    List<OrderBrandStatisticDto> getOrderStatisticByBrand();

    List<Map<String, Object>> getOrderProductByOrder(@Param("start")Date start, @Param("end") Date end, @Param("province") String province);

    List<Map<String, Object>> getOrderSpuStatistic(@Param("start") Date start, @Param("end") Date end);

    List<Map<String, Object>> totalOrderUser(@Param("start") Date start, @Param("end") Date end);

    List<Map<String, Object>> newOrderUser(@Param("start") Date start, @Param("end") Date end);

    List<Map<String, Object>> oldOrderUser(@Param("start") Date start, @Param("end") Date end);

    List<OrderProductDto> getOrderProductStatistic(@Param("page") Integer page, @Param("pageSize") Integer pageSize, @Param("list") List<Long> list);

    Long getOrderProductStatisticCount(@Param("list") List<Long> list);

    List<Long> getUserIds(@Param("start") Date start, @Param("end") Date end, @Param("province") String province, @Param("category") String category);
}
