package com.rrk.order.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.rrk.common.constant.OrderContants;
import com.rrk.common.constant.OrderTypeEnum;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.order.dto.KillOrderVo;
import com.rrk.common.utils.DateUtils;
import com.rrk.order.service.KillOrderService;
import com.rrk.order.utils.OrderUtil;
import com.rrk.order.utils.RabbitMqUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service("KillOrderService")
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class KillOrderServiceImpl implements KillOrderService {

    @Autowired
    private RabbitMqUtil rabbitMqUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 秒杀下单处理
     *
     * @param userId
     * @param skuId
     * @return
     */
    @Override
    public Integer addKillOrder(Long userId, Long skuId) {
        Integer result = 1;

        log.info("秒杀下单传入的参数：userId->{},skuId->{}", userId, skuId);
        //创建锁
        RLock lock = redissonClient.getLock(skuId.toString());
       // userId = 1000L + Convert.toLong(new Random().nextInt(100));
        /**
         * 初始预扣库存的值
         */
        Integer yuStock = 0;
        Integer killStock = 0;
        Integer sellStock = 0;
        Integer initStock = 0;
        //2，秒杀商品仅限一个用户买一个
        String userSkuKey = userId + "_" + skuId;
        //1，根据skuId从redis中获取秒杀商品信息
        Object o = redisTemplate.opsForHash().get(RedisConstant.KILL_SKU_KEY, skuId.toString());
        if (ObjectUtil.isNull(o)) {
            //活动已结束
            throw new MyException(452, "活动已结束");
        }
        Object orderObject = redisTemplate.opsForHash().get(RedisConstant.KILL_ORDER_KEY, userSkuKey);
        if (ObjectUtil.isNotNull(orderObject)) {
            //您已抢单成功
            throw new MyException(452, "已抢单成功");
        }
        String[] skuArr = o.toString().split("_");
        //该秒杀商品剩余库存
        killStock = Convert.toInt(skuArr[0]);
        yuStock = Convert.toInt(skuArr[0]);
        //实际销售的库存(秒杀商品不支持退款，只有支付成功的商品才算售出)
        sellStock = Convert.toInt(skuArr[1]);
        //该秒杀商品初始库存
        initStock = Convert.toInt(skuArr[2]);
        BigDecimal amount = Convert.toBigDecimal(skuArr[3]);
        KillOrderVo orderVo = new KillOrderVo();
        try {
            //尝试获取锁（最多尝试 10s,获取锁后15s自动释放）
            boolean flag = lock.tryLock(15, 45, TimeUnit.SECONDS);
            if (!flag) {
                log.info("获取锁失败：skuId->{}", skuId);
                return 3;
            }
            if (killStock <= 0) {
                //抢单人数太多
                result = 4;
                // throw new MyException(453,"系统繁忙，请稍后再试");
                return result;
            }
            //3,判断是否售罄
            if (sellStock >= initStock) {
                //该商品已售罄
                result = 5;
                //throw new MyException(454,"已售罄");
                return result;
            }
            //4.将用户订单先存到redis
            String orderCode = OrderUtil.getOrderCode(OrderTypeEnum.getTypeValue(OrderContants.KILL_ORDER_TYPE), userId);
            // String value = orderCode + "_" + userId + "_" + skuId;
            orderVo.setAmount(amount);
            orderVo.setCreateTime(new Date());
            Date d = new Date();
            long time = System.currentTimeMillis() + 5 * 60 * 1000;
            d.setTime(time);
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
            orderVo.setOverTime(DateUtils.parseDate(format,DateUtils.DATE_TIME_PATTERN));
            orderVo.setOrderNo(orderCode);
            orderVo.setSkuId(skuId);
            orderVo.setUserId(userId);
            // int i = 1/0;
            //redisTemplate.opsForHash().put(RedisConstant.KILL_ORDER_KEY, userSkuKey, value);
            //5，修改剩余库存（扣减库存）
            killStock--;
            //6,抢单成功后，将订单放到延迟队列（设置下单成功后超时30分钟未支付将清空订单并库存回滚）
            redisTemplate.opsForHash().put(RedisConstant.KILL_ORDER_KEY, userSkuKey, JSON.toJSONString(orderVo));
            // Thread.sleep(6000);
            redisTemplate.opsForHash().put(RedisConstant.KILL_SKU_KEY, skuId.toString(), killStock + "_" + sellStock + "_" + initStock + "_" + amount);
            rabbitMqUtil.killOrderQueue(orderCode, orderVo);
            //int i = 1/0;
            yuStock--;
            return 7;
        } catch (Exception e) {
            log.error("秒杀下单失败：e->{}", e);
            //  result = 8;
            throw new MyException("下单失败");
            // return 0;
        } finally {
            //手动回滚redis事务
            if (yuStock.intValue() != killStock.intValue()) {
                killStock++;
                redisTemplate.opsForHash().put(RedisConstant.KILL_SKU_KEY, skuId.toString(), killStock + "_" + sellStock + "_" + initStock + "_" + amount);
                redisTemplate.opsForHash().delete(RedisConstant.KILL_ORDER_KEY, userSkuKey);
            }
            lock.unlock();
        }
    }
}
