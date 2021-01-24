package com.rrk.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.codingapi.txlcn.tc.annotation.DTXPropagation;
//import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.rrk.common.constant.OrderTypeEnum;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.dto.PageDto;
import com.rrk.common.handle.MyException;
import com.rrk.common.handle.OrderException;
import com.rrk.common.modules.order.dao.TbOrderMapper;
import com.rrk.common.modules.order.dto.AliNotifyResDto;
import com.rrk.common.modules.order.dto.GeneralOrderDto;
import com.rrk.common.modules.order.dto.OrderDto;
import com.rrk.common.modules.order.dto.OrderInfo;
import com.rrk.common.modules.order.entity.OrderSku;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.common.modules.order.entity.TbPayLog;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.common.utils.ToolUtil;
import com.rrk.order.fegin.ProductFeginClient;
import com.rrk.order.fegin.UserFeginClient;
import com.rrk.order.service.IOrderSkuService;
import com.rrk.order.service.ITbOrderService;
import com.rrk.order.service.ITbPayLogService;
import com.rrk.order.utils.OrderUtil;
import com.rrk.order.utils.RabbitMqUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单主表 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbOrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder> implements ITbOrderService {

    @Autowired
    private ITbOrderService orderService;

    @Autowired
    private IOrderSkuService orderSkuService;

    @Autowired
    private ProductFeginClient productFeginClient;

    //private static Integer COUNT = 50;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RabbitMqUtil rabbitMqUtil;
    @Autowired
    private ITbPayLogService payLogService;

    @Autowired
    private UserFeginClient userFeginClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbOrderMapper orderMapper;

    // private Lock lock = new ReentrantLock();

    // private  AtomicInteger atomicInteger =  new AtomicInteger(COUNT);

    /**
     * 生成订单号
     *
     * @param userId
     * @param
     * @return
     */
    @Override
    public String getOrderId(Long userId) {
        String orderNo = OrderUtil.getOrderCode(OrderTypeEnum.getTypeValue(0), userId);
        return orderNo;
    }

    /**
     * 用户下单
     *
     * @param orderDto
     * @return
     */

//    @LcnTransaction(propagation = DTXPropagation.REQUIRED) //事务发起方
    @Override
    public Integer addOrder(OrderDto orderDto, Long userId) {
        log.info("下单传入的参数：orderDto->{},userId->{}", orderDto, userId);
        /**
         *  1 如何保证订单的幂等性（重复下单的问题）(已解决)
         *  2，并发的情况下如何防止超卖（分布式的情况下 分布式锁的问题）解决
         *  3,如何保证下单失败时，扣减库存能回滚（分布式事务问题 已解决）
         *  4,未支付的订单超时如何自动处理 解决
         */
        //1,获取锁
        RLock lock = redissonClient.getLock(orderDto.getSkuId().toString());
        try {

            /**
             * 加进程锁synchronized 是否能解决集群下的超卖问题（采用 jmeter软件工具测试）
             *
             */
            //2,加锁（超时时间45s）
            lock.lock(45, TimeUnit.SECONDS);
            // String orderCode = OrderUtil.getOrderCode(OrderTypeEnum.getTypeValue(OrderContants.PO_ORDER_TYPE), userId);
            // orderDto.setOrderNo(orderCode);
            //1 获取要被购买的产品sku
            TbSku sku = productFeginClient.getSkuById(orderDto.getSkuId());
            if (ObjectUtil.isNull(sku)) {
                //要购买的商品sku不存在
                throw new MyException("商品已下架");
            }
            if (sku.getId() == -1L) {
                throw new MyException("系统繁忙，稍后再试");
            }
            if(sku.getId() == -2L){
                throw new MyException("获取商品失败");
            }

            if (sku.getStock() - orderDto.getNum() < 0) {
                throw new MyException(sku.getTitle() + "库存不足");
            }
            //2封装数据(订单主表 和 订单商品表)
            TbOrder tbOrder = new TbOrder(userId, sku, orderDto);
            OrderSku orderSku = new OrderSku(userId, sku, orderDto);
            boolean save = orderService.save(tbOrder);
            //验证本地事务
            //int i = 1/0;
            boolean saveOrderSku = orderSkuService.save(orderSku);
            //3修改库存
            sku.setStock(sku.getStock() - orderDto.getNum());
            sku.setSaleCount((sku.getSaleCount() == null ? 0 : sku.getSaleCount()) + orderDto.getNum());
            Integer updateStock = productFeginClient.updateStock(sku);
            //验证分布式事务((没加分布式事务)这种情况会出现 用户下单失败，但是库存确被扣减了 商家亏了)
            //int i = 1/0;
            //todo:4，用户下单发送消息后期补充
            if (save && saveOrderSku && (updateStock > 0)) {
                log.info("恭喜用户：{}下单成功，剩余库存： {}", userId, sku.getStock());
                //发送到队列
                rabbitMqUtil.orderAddQueue(orderDto);
                //将订单对应的商品存入es用户行为分析的索引库中
                rabbitMqUtil.sendUserMessage(userId,sku.getTitle());
                return 1;
            } else if(updateStock == -1){
                throw new MyException("系统繁忙，稍后再试");
            } else if(updateStock == -2){
                throw new MyException("下单失败");
            }
            return 0;
        } catch (DuplicateKeyException de) {
            throw new MyException("已下单");
        } finally {
            //释放锁
            lock.unlock();
        }
        //第一种用 synchronized
//        synchronized (this){
//            COUNT --;
//        }
        //第二种 ReentrantLock
//        try {
//           lock.lock();
//           COUNT --;
//        }finally {
//            lock.unlock();
//        }
// 第三种使用原子类

// int count = atomicInteger.decrementAndGet();
        // return 0;
    }

    /**
     * 修改订单状态
     *
     * @param aliNotifyResDto
     * @return
     */
    @Override
    public Boolean updateOrderStaus(AliNotifyResDto aliNotifyResDto) {
        //1，判断要修改的订单是否存在
        try {
            TbOrder order = orderService.getOne(new QueryWrapper<TbOrder>().eq("order_no", aliNotifyResDto.getOrderId()));
            if (ObjectUtil.isNull(order)) {
                log.error("要支付回调的订单不存在：order->{}", order);
                throw new OrderException(556, "订单不存在" + order);
            }
            //2，判断订单是否已经支付
            if (order.getOrderStatus() != 0) {
                log.error("该订单已支付： order->{}", order);
                throw new OrderException(556, "该订单已支付");
            }
            order.setOrderStatus(1);
            order.setPayTime(aliNotifyResDto.getPayTime());
            order.setPayType(0);
            boolean orderUpdate = orderService.updateById(order);
            //3，记录支付日志
            TbPayLog tbPayLog = new TbPayLog();
            tbPayLog.setOrderNo(aliNotifyResDto.getOrderId());
            tbPayLog.setPayAmount(aliNotifyResDto.getTotalAmount());
            tbPayLog.setPayId(aliNotifyResDto.getPayOrderId());
            tbPayLog.setPayTime(aliNotifyResDto.getPayTime());
            boolean save = payLogService.save(tbPayLog);
            return save && orderUpdate == true ? true : false;
        } catch (OrderException e) {
            log.error("支付宝支付回调业务处理失败：e->{}",e);
            throw new OrderException(556,"支付回调业务处理异常");
        }
    }

    /**
     * 获取订单详情
     * @param orderNo
     * @return
     */
    @Override
    public GeneralOrderDto getOrderByOrderNo(String orderNo) {
         //1获取订单数据
        TbOrder order = orderService.getOne(new QueryWrapper<TbOrder>().eq("order_no", orderNo));
        //2，获取订单商品
        List<OrderSku> orderSkus = orderSkuService.list(new QueryWrapper<OrderSku>().eq("order_no", orderNo));
        //3，获取用户收货人信息
        TbUserAddress address =null;
        List<TbUserAddress> addressList = new ArrayList<>();
        Object o = redisTemplate.opsForHash().get(RedisConstant.USER_ADDRESS_KEY, order.getUserId().toString());
        if (ObjectUtil.isNotNull(o)) {
            addressList = JSON.parseArray(o.toString(), TbUserAddress.class);
            address = addressList.stream().filter(a -> ToolUtil.equals(a.getId(), order.getAddressId())).findFirst().orElse(null);
        }
        if (ObjectUtil.isNull(address)) {
            address = userFeginClient.getUserAddressById(order.getUserId(), order.getAddressId());
            if (ObjectUtil.isNotNull(address)) {
                if (address.getId() == -1L) {
                    throw new MyException("系统繁忙，请稍后再试");
                }
                if (address.getId() == -2L) {
                    throw new MyException("获取用户地址失败");
                }
                addressList.add(address);
                redisTemplate.opsForHash().put(RedisConstant.USER_ADDRESS_KEY,order.getUserId().toString(),JSON.toJSONString(addressList));
            } else {
                throw new MyException("用户地址不存在");
            }

        }
        GeneralOrderDto generalOrderDto = new GeneralOrderDto(order,orderSkus,address);
        return generalOrderDto;
    }

    /**
     * 获取订单列表
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public IPage<OrderInfo> getOrderInfoList(Long userId, Integer pageNo, Integer pageSize) {
        Page<OrderInfo> page = new Page<>(pageNo,pageSize);
        PageDto pageDto = new PageDto(pageNo, pageSize);
        List<OrderInfo> list = orderMapper.getOrderInfoList(userId,pageDto.getPage(),pageDto.getPageSize());
        page.setRecords(list);
        return page;
    }
}
