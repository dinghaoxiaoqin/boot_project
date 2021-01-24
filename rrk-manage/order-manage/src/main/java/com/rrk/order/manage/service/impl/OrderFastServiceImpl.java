package com.rrk.order.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.order.dao.OrderFastMapper;
import com.rrk.common.modules.order.entity.OrderFast;
import com.rrk.order.manage.service.IOrderFastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单快递信息 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OrderFastServiceImpl extends ServiceImpl<OrderFastMapper, OrderFast> implements IOrderFastService {

}
