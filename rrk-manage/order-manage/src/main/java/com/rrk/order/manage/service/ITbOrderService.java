package com.rrk.order.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.dto.OrderCategoryDto;
import com.rrk.common.dto.OrderProductDto;
import com.rrk.common.dto.OrderProvinceDto;
import com.rrk.common.dto.OrderUserStatisticDto;
import com.rrk.common.modules.order.dto.OrderBrandStatisticDto;
import com.rrk.common.modules.order.dto.ProvinceDto;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.order.manage.dto.*;

import java.util.List;

/**
 * <p>
 * 订单主表 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
public interface ITbOrderService extends IService<TbOrder> {

    IPage<TbOrder> getOrderList(Integer pageNo, Integer pageSize, Integer orderStatus, String startTime, String endTime, String orderNo);

    OrderFastDto getDelieverOrder(Long id);

    Integer deliveryOrder(List<OrderFastDto> list,Long userId);

    OrderDetailDto getOrderDetail(Long orderId);

    List<TraceDto> getOrderFast(String orderNo);

    Boolean closeOrder(CloseOrderDto closeOrderDto,Long userId);

    Boolean deleteOrder(List<Long> ids, Long userId);

    Boolean orderClose(Long orderId, Long userId);

    List<OrderBrandStatisticDto> getStatisticByBrand(String startTime, String endTime, String province, String category);

    List<OrderBrandStatisticDto> getOrderStatisticByBrand();

    List<OrderCategoryDto> getOrderProductByOrder(String startTime, String endTime, String category);

    List<OrderProvinceDto> getOrderSpuStatistic(String startTime, String endTime);

    List<OrderUserStatisticDto> getOrderUserStatistic(String startTime, String endTime);

    IPage<OrderProductDto> getOrderProductStatistic(String startTime, String endTime, String province, String category, Integer pageNo, Integer pageSize);

    List<ProvinceDto> getProvinceSku(String startTime, String endTime, String category);
}
