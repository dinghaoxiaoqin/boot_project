package com.rrk.order.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.modules.order.dao.OrderSkuMapper;
import com.rrk.common.modules.order.dto.KillOrderVo;
import com.rrk.common.modules.order.entity.OrderSku;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.order.fegin.ProductFeginClient;
import com.rrk.order.fegin.UserFeginClient;
import com.rrk.order.service.IOrderSkuService;
import com.rrk.order.service.ITbOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IOrderSkuService orderSkuService;

    @Autowired
    private ITbOrderService orderService;

    @Autowired
    private ProductFeginClient productFeginClient;

    @Autowired
    private UserFeginClient userFeginClient;

    /**
     * 秒杀订单处理
     *
     * @param orderVo
     */
    @Override
    public void addKillOrder(Integer orderStatus, KillOrderVo orderVo) {
        String userSkuKey = orderVo.getUserId() + "_" + orderVo.getSkuId();
        //库存回滚 和清空redis的这个订单
        TbSku sku = productFeginClient.getSkuById(orderVo.getSkuId());
        TbOrder order = new TbOrder(orderVo, orderStatus);
        OrderSku orderSku = new OrderSku(sku,orderVo,orderStatus);
        orderService.save(order);
        orderSkuService.save(orderSku);
        redisTemplate.opsForHash().delete(RedisConstant.KILL_ORDER_KEY, userSkuKey);
        Object o = redisTemplate.opsForHash().get(RedisConstant.KILL_SKU_KEY, orderVo.getSkuId().toString());
        if (ObjectUtil.isNotNull(o)) {
            String[] skuArr = o.toString().split("_");
            Integer killStock = Convert.toInt(skuArr[0]);
            killStock++;
            String key = killStock + "_" + skuArr[1] + "_" + skuArr[2] + "_"+skuArr[3];
            redisTemplate.opsForHash().put(RedisConstant.KILL_SKU_KEY, orderVo.getSkuId().toString(), key);
        }

    }
}
