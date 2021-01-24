package com.rrk.order.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.modules.order.entity.OrderSku;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.common.modules.product.dto.webdto.SkuDto;
import com.rrk.common.modules.user.dto.webdto.CityDto;
import com.rrk.common.modules.user.dto.webdto.DistrictDto;
import com.rrk.common.modules.user.dto.webdto.ProvinceDto;
import com.rrk.common.modules.user.dto.webdto.RegionDto;
import com.rrk.common.utils.ToolUtil;
import com.rrk.order.fegin.ProductFeginClient;
import com.rrk.order.fegin.UserFeginClient;
import com.rrk.order.service.IOrderSkuService;
import com.rrk.order.service.ITbOrderService;
import com.rrk.order.utils.ChineseName;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
@JobHandler(value = "OrderJobHandler")
public class OrderJobHandler extends IJobHandler {

    @Autowired
    private IOrderSkuService orderSkuService;

    @Autowired
    private ITbOrderService orderService;

    @Autowired
    private ProductFeginClient productFeginClient;

    @Autowired
    private UserFeginClient userFeginClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("------------------------随机下单开始了----------------------------");
        List<TbOrder> orders = new ArrayList<>();
        List<OrderSku> orderSkus = new ArrayList<>();
        RegionDto regions;
        //获取所有的省市区
        String s = redisTemplate.opsForValue().get(RedisConstant.AREA_KEY);
        if (StrUtil.isBlank(s)) {
            regions = userFeginClient.getRegions();
            redisTemplate.opsForValue().set(RedisConstant.AREA_KEY, JSON.toJSONString(regions));
        } else {
            regions = JSON.parseObject(s, RegionDto.class);
        }
        if (ObjectUtil.isNotNull(regions)) {
            for (int i = 0; i < 60; i++) {
                int totalCount = 0;
                BigDecimal amount = BigDecimal.ZERO;
                //随机县区编号
                int areaCode = new Random().nextInt(110099) + 710200;
                List<DistrictDto> districtDtoList = regions.getDistrictDtoList();
                String s2 = redisTemplate.opsForValue().get(RedisConstant.DISTINICT_KEY);
                List<Integer> list = JSON.parseArray(s2, Integer.class);
                int idx = new Random().nextInt(list.size() + 1);
                DistrictDto districtDto = districtDtoList.stream().filter(d -> ToolUtil.equals(d.getCode(), list.get(idx))).findFirst().orElse(null);
                if (ObjectUtil.isNull(districtDto)) {
                    continue;
                } else {
                    CityDto cityDto = regions.getCityDtoList().stream().filter(r -> ToolUtil.equals(districtDto.getParentCode(), r.getCode())).findFirst().orElse(null);
                    ProvinceDto provinceDto = regions.getProvinceDtoList().stream().filter(p -> ToolUtil.equals(cityDto.getParentCode(), p.getCode())).findFirst().orElse(null);
                    TbOrder order = new TbOrder();
                    //随机生成订单号
                    int userId = new Random().nextInt(360000) + 10000;
                    String orderNo = orderService.getOrderId(Convert.toLong(userId));
                    order.setOrderNo(orderNo);
                    order.setUserId(Convert.toLong(userId));
                    order.setCreateTime(new Date());
                    order.setReceiverArea(districtDto.getName());
                    order.setReceiverProvince(provinceDto.getName());
                    order.setReceiverCity(cityDto.getName());
                    order.setReceiverAddress(provinceDto.getName() + cityDto.getName() + districtDto.getName());
                    order.setReceiverName(ChineseName.getUserName());
                    order.setReceiverPhone(Convert.toStr(new Random().nextInt(888888888) + 1000000000));
                    Integer shopId = new Random().nextInt(100);
                    order.setShopId(Convert.toLong(shopId));
                    order.setAddressId(0L);
                    int j = new Random().nextInt(5)+1;
                    for (int k = 0; k <j ; k++) {
                        OrderSku orderSku = new OrderSku();
                        //随机获取商品skuId
                        String s1 = redisTemplate.opsForValue().get(RedisConstant.SKU_KEY);
                        List<Long> longs = JSON.parseArray(s1, Long.class);
                        int index = new Random().nextInt(longs.size() + 1);
                        SkuDto skuDto = productFeginClient.getSkuBySkuId(longs.get(index));
                        orderSku.setOrderNo(orderNo);
                        orderSku.setCreateTime(new Date());
                        orderSku.setPrice(skuDto.getPrice());
                        orderSku.setSalePrice(skuDto.getSalePrice());
                        orderSku.setProductBrand(skuDto.getProductBrand());
                        orderSku.setProductCategory(skuDto.getProductCategory());
                        orderSku.setSkuDesc(skuDto.getSkuDesc());
                        orderSku.setSkuId(skuDto.getSkuId());
                        orderSku.setSkuImage(skuDto.getSkuImage());
                        orderSku.setSkuName(skuDto.getSkuName());
                        orderSku.setSkuNum(new Random().nextInt(4)+1);
                        orderSku.setSpuName(skuDto.getSpuName());
                        totalCount = totalCount+orderSku.getSkuNum();
                        amount = amount.add(orderSku.getSalePrice());
                        orderSkus.add(orderSku);
                    }
                    order.setAmount(amount);
                    order.setBuyNum(totalCount);
                   int orderStatus =  new Random().nextInt(5);
                    order.setOrderStatus(orderStatus);
                    if (orderStatus == TbOrder.NO_PAY_ORDER) {
                        order.setPayType(0);
                        order.setIsDelete(0);

                    } else  if(orderStatus == TbOrder.WAIT_DELIVER_ORDER){
                        int random = new Random().nextInt(2);
                        if (random == 0) {
                            order.setPayType(1);
                        } else {
                            order.setPayType(2);
                        }
                        order.setIsDelete(0);
                        order.setPayTime(DateUtil.offsetDay(new Date(),1));
                    } else if(orderStatus == TbOrder.WAIT_RECIVER_ORDER){
                        int random = new Random().nextInt(2);
                        if (random == 0) {
                            order.setPayType(1);
                        } else {
                            order.setPayType(2);
                        }
                        order.setIsDelete(0);
                        order.setPayTime(DateUtil.offsetDay(new Date(),1));
                        order.setSendTime(DateUtil.offsetDay(new Date(),2));

                    } else if(orderStatus == TbOrder.FINISH_ORDED){
                        int random = new Random().nextInt(2);
                        if (random == 0) {
                            order.setPayType(1);
                        } else {
                            order.setPayType(2);
                        }
                        order.setIsDelete(0);
                        order.setPayTime(DateUtil.offsetDay(new Date(),1));
                        order.setSendTime(DateUtil.offsetDay(new Date(),2));
                        order.setFinishTime(DateUtil.offsetDay(new Date(),3));
                    } else if(orderStatus == TbOrder.CLOSE_ORDER){
                        order.setIsDelete(0);
                        order.setPayType(0);
                        int random = new Random().nextInt(2);
                        if (random == 0) {
                            order.setOverTime(DateUtil.offsetDay(new Date(),1));
                        }
                    }
                    orders.add(order);

                }

            }
        }
        if (CollUtil.isNotEmpty(orders) && CollUtil.isNotEmpty(orderSkus)) {
            orderService.saveBatch(orders);
            orderSkuService.saveBatch(orderSkus);
        }
        log.info("随机下单结束了----------------------------");
        return ReturnT.SUCCESS;
    }
}
