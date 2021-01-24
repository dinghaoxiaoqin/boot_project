package com.rrk.order.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.order.dao.OrderSkuMapper;
import com.rrk.common.modules.order.entity.OrderSku;
import com.rrk.order.manage.service.IOrderSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OrderSkuServiceImpl extends ServiceImpl<OrderSkuMapper, OrderSku> implements IOrderSkuService {

}
