package com.rrk.order.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.constant.ElasticsearchContants;
import com.rrk.common.dto.*;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.order.dao.TbOrderMapper;
import com.rrk.common.modules.order.dto.OrderBrandStatisticDto;
import com.rrk.common.modules.order.dto.ProvinceDto;
import com.rrk.common.modules.order.entity.OrderFast;
import com.rrk.common.modules.order.entity.OrderSku;
import com.rrk.common.modules.order.entity.TbOperatorLog;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.common.utils.DateUtils;
import com.rrk.common.utils.ToolUtil;
import com.rrk.order.manage.dto.*;
import com.rrk.order.manage.service.*;
import com.rrk.order.manage.utils.ElasticsearchUtil;
import com.rrk.order.manage.utils.KdNiaoUtil;
import com.rrk.order.manage.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ValueCountAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.BucketSelectorPipelineAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private IOrderFastService orderFastService;

    @Autowired
    private ITbUserService userService;

    @Autowired
    private ITbUserAddressService userAddressService;

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private ITbOperatorLogService operatorLogService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取订单列表
     *
     * @param pageNo
     * @param pageSize
     * @param orderStatus
     * @param startTime
     * @param endTime
     * @param orderNo
     * @return
     */
    @Override
    public IPage<TbOrder> getOrderList(Integer pageNo, Integer pageSize, Integer orderStatus, String startTime, String endTime, String orderNo) {
        log.info("订单列表传入的参数：pageNo->{},pageSize->{},orderStatus->{},startTime->{},endTime->{},orderNo->{}", pageNo, pageSize, orderStatus, startTime, endTime, orderNo);
        Page<TbOrder> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TbOrder> wrapper = new QueryWrapper<>();
        //wrapper.eq("is_delete",TbOrder.NO_DELETE);
        if (StrUtil.isNotBlank(orderNo)) {
            wrapper.like("order_no", orderNo);
        }
        if (startTime != null && endTime != null) {
            wrapper.between("create_time", startTime, endTime);
        } else if (startTime == null && endTime != null) {
            wrapper.le("create_time", endTime);
        } else if (startTime != null && endTime == null) {
            wrapper.ge("create_time", startTime);
        }
        if (orderStatus != null) {
            wrapper.eq("order_status", orderStatus);
        }
        wrapper.orderByDesc("create_time");
        IPage<TbOrder> orderIPage = orderService.page(page, wrapper);
        return orderIPage;
    }

    /**
     * 获取发货订单数据
     */
    @Override
    public OrderFastDto getDelieverOrder(Long id) {

        log.info("获取订单传入的参数：id->{}", id);
        //1获取订单数据
        TbOrder order = orderService.getOne(new QueryWrapper<TbOrder>().eq("order_status", TbOrder.WAIT_DELIVER_ORDER).eq("id", id));
        if (ObjectUtil.isNull(order)) {
            throw new MyException("该订单不存在，请联系管理员");
        }
        //2，获取发货人的数据
        TbUser user = userService.getOne(new QueryWrapper<TbUser>().eq("user_id", order.getUserId()));
        //3,获取发货人的地址
        TbUserAddress userAddress = userAddressService.getOne(new QueryWrapper<TbUserAddress>().eq("address_id", order.getAddressId()));
        OrderFastDto orderFastDto = new OrderFastDto(order, user, userAddress);
        return orderFastDto;
    }

    /**
     * 订单发货
     */
    @Override
    public Integer deliveryOrder(List<OrderFastDto> list, Long userId) {
        //获取用户角色数据
        String partName = redisUtil.getPartName(userId);
        List<TbOperatorLog> operatorLogs = new ArrayList<>();
        List<TbOrder> orders = new ArrayList<>();
        List<OrderFast> orderFasts = new ArrayList<>();
        for (OrderFastDto orderFastDto : list) {
            TbOrder order = new TbOrder();
            //修改订单状态
            order.setId(orderFastDto.getOrderId());
            order.setSendTime(new Date());
            order.setOrderNo(orderFastDto.getOrderNo());
            order.setOrderStatus(TbOrder.WAIT_RECIVER_ORDER);
            orders.add(order);
            //记录发货的订单物流数据
            OrderFast orderFast = new OrderFast();
            orderFast.setCreateTime(new Date());
            orderFast.setExpressNo(orderFastDto.getExpressNo());
            orderFast.setLogisticCode(orderFastDto.getLogisticCode());
            orderFast.setLogisticName(orderFastDto.getLogisticName());
            orderFast.setOrderNo(orderFastDto.getOrderNo());
            orderFasts.add(orderFast);
            //添加操作日志
            TbOperatorLog log = new TbOperatorLog();
            log.setOrderNo(orderFastDto.getOrderNo());
            log.setCreateTime(new Date());
            log.setOrderStatus(TbOrder.WAIT_RECIVER_ORDER);
            log.setOperatorBy(partName);
            operatorLogs.add(log);

        }
        boolean b = orderService.updateBatchById(orders);
        boolean fastFlag = orderFastService.saveBatch(orderFasts);
        boolean operatorFlag = operatorLogService.saveBatch(operatorLogs);
        if (b && fastFlag && operatorFlag) {
            return 1;
        } else {
            return 0;
        }
        // return 1;
    }

    /**
     * 获取订单详情数据
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailDto getOrderDetail(Long orderId) {
        log.info("订单详情传入的参数：orderId->{}", orderId);
        TbOrder one = orderService.getOne(new QueryWrapper<TbOrder>().eq("id", orderId));
        if (ObjectUtil.isNull(one)) {
            throw new MyException("该订单不存在");
        }
        List<OrderSku> list = orderSkuService.list(new QueryWrapper<OrderSku>().eq("order_no", one.getOrderNo()));
        List<TbOperatorLog> logs = operatorLogService.list(new QueryWrapper<TbOperatorLog>().eq("order_no", one.getOrderNo()));
        OrderFast orderFast = orderFastService.getOne(new QueryWrapper<OrderFast>().eq("order_no", one.getOrderNo()));
        OrderDetailDto orderDetailDto = new OrderDetailDto(one, orderFast, list, logs);
        return orderDetailDto;
    }

    /**
     * 获取订单物流数据
     *
     * @param orderNo
     * @return
     */
    @Override
    public List<TraceDto> getOrderFast(String orderNo) {
        log.info("获取订单跟踪传入的参数：orderNo->{}", orderNo);
        OrderFast orderFast = orderFastService.getOne(new QueryWrapper<OrderFast>().eq("order_no", orderNo));
        if (ObjectUtil.isNull(orderFast)) {
            throw new MyException("暂无该订单物流数据");
        }
        String orderTracesByJson = KdNiaoUtil.getOrderTracesByJson(orderFast.getLogisticCode(), orderFast.getExpressNo());
        KdNiaoDto kdNiaoDto = JSON.parseObject(orderTracesByJson, KdNiaoDto.class);
        if ("false".equals(kdNiaoDto.getSuccess())) throw new MyException("暂不支持该运单查询，请到官网查询");
        List<TraceDto> traces = kdNiaoDto.getTraces();
        return traces;
    }

    /**
     * 添加订单备注
     *
     * @param closeOrderDto
     * @return
     */
    @Override
    public Boolean closeOrder(CloseOrderDto closeOrderDto, Long userId) {
        log.info("添加备注前端传来的参数：closeOrderDto->{},userId->{}", closeOrderDto, userId);
        String partName = redisUtil.getPartName(userId);
        if (closeOrderDto.getDialogVisible()) {
            TbOrder order = orderService.getOne(new QueryWrapper<TbOrder>().eq("id", closeOrderDto.getOrderId()));
            if (ObjectUtil.isNotNull(order)) {
                order.setRemarks(closeOrderDto.getContent());
                order.setUpdateTime(new Date());
                //添加操作日志
                TbOperatorLog log = new TbOperatorLog();
                log.setOrderNo(order.getOrderNo());
                log.setCreateTime(new Date());
                log.setOrderStatus(order.getOrderStatus());
                log.setOperatorBy(partName);
                log.setRemarks(closeOrderDto.getContent());
                orderService.updateById(order);
                operatorLogService.save(log);
                return true;
            } else {
                throw new MyException("该订单不存在");
            }
        } else {
            throw new MyException("后台已取消订单备注");
        }

    }

    /**
     * 删除订单
     *
     * @param ids
     * @param userId
     * @return
     */
    @Override
    public Boolean deleteOrder(List<Long> ids, Long userId) {
        log.info("删除订单前端传来的参数：ids->{},userId->{}", ids, userId);
        if (CollUtil.isEmpty(ids)) {
            throw new MyException("请选择要删除的订单");
        }
        String partName = redisUtil.getPartName(userId);
        List<TbOperatorLog> operatorLogs = new ArrayList<>();
        List<TbOrder> orders = orderService.list(new QueryWrapper<TbOrder>().in("id", ids));
        if (CollUtil.isEmpty(orders)) {
            throw new MyException("要删除的订单不存在");
        }
        for (TbOrder order : orders) {
            order.setUpdateTime(new Date());
            order.setIsDelete(TbOrder.DELETE);
            order.setOrderStatus(TbOrder.CLOSE_ORDER);
            TbOperatorLog log = new TbOperatorLog();
            log.setRemarks("删除订单");
            log.setOrderNo(order.getOrderNo());
            log.setOperatorBy(partName);
            log.setOrderStatus(TbOrder.CLOSE_ORDER);
            log.setCreateTime(new Date());
            operatorLogs.add(log);
        }
        boolean b = orderService.updateBatchById(orders);
        boolean batch = operatorLogService.saveBatch(operatorLogs);
        if (b && batch) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 关闭订单
     *
     * @param orderId
     * @param userId
     * @return
     */
    @Override
    public Boolean orderClose(Long orderId, Long userId) {
        log.info("关闭订单前端传来的参数：orderId->{},userId->{}", orderId, userId);
        TbOrder order = orderService.getOne(new QueryWrapper<TbOrder>().eq("id", orderId));
        if (ObjectUtil.isNull(order)) {
            throw new MyException("要关闭的订单不存在");
        }
        String partName = redisUtil.getPartName(userId);
        order.setOrderStatus(TbOrder.CLOSE_ORDER);
        order.setUpdateTime(new Date());
        TbOperatorLog log = new TbOperatorLog();
        log.setCreateTime(new Date());
        log.setOrderStatus(TbOrder.CLOSE_ORDER);
        log.setOperatorBy(partName);
        log.setRemarks("关闭订单");
        log.setOrderNo(order.getOrderNo());
        boolean save = operatorLogService.save(log);
        boolean b = orderService.updateById(order);
        if (save && b) {
            return true;
        }
        return false;
    }

    /**
     * 按照品牌分类来统计订单数据
     *
     * @param startTime
     * @param endTime
     * @param province
     * @param category
     * @return
     */
    @Override
    public List<OrderBrandStatisticDto> getStatisticByBrand(String startTime, String endTime, String province, String category) {
        List<OrderBrandStatisticDto> list = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            //从es里面获取数据
            BoolQueryBuilder bool = QueryBuilders.boolQuery();
            List<QueryBuilder> must = bool.must();
            RangeQueryBuilder rangeStatus = QueryBuilders.rangeQuery("orderstatus").gte(1).lte(3);
            RangeQueryBuilder rangeTime = QueryBuilders.rangeQuery("createtime").gte(start).lte(end);
            must.add(rangeStatus);
            must.add(rangeTime);
            if (StrUtil.isNotBlank(province)) {
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("receiverprovince.keyword", province);
                must.add(termQueryBuilder);
            }
            if (StrUtil.isNotBlank(category)) {
                TermQueryBuilder productcategory = QueryBuilders.termQuery("productcategory", category);
                must.add(productcategory);
            }
            TermsAggregationBuilder builder = AggregationBuilders.terms("brand_term").field("productbrand").size(50);
            //嵌套聚合
            SumAggregationBuilder sum = AggregationBuilders.sum("sku_nums").field("skunum");
            SumAggregationBuilder sumAmount = AggregationBuilders.sum("sum_amount").field("amount");
            builder.subAggregation(sum).subAggregation(sumAmount);
            list = ElasticsearchUtil.getAggBrand(ElasticsearchContants.ORDER_INDEX, bool, builder);
            if (CollUtil.isEmpty(list)) {
                list = orderMapper.getStatisticByBrand(start, end, province, category);
            }

        } catch (Exception e) {
            log.error("获取品牌订单统计数据异常：e->{}", e);
        }
        return list;
    }

    /**
     * 按照品牌统计订单数据
     */
    @Override
    public List<OrderBrandStatisticDto> getOrderStatisticByBrand() {
        return orderMapper.getOrderStatisticByBrand();
    }

    /**
     * 查询每个商品分类下销量最好的商品信息(要是通过es则查询前10的商品)
     *
     * @param startTime
     * @param endTime
     * @param category
     * @return
     */
    @Override
    public List<OrderCategoryDto> getOrderProductByOrder(String startTime, String endTime, String category) {
        List<OrderCategoryDto> list = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            //从es 中获取
            BoolQueryBuilder bool = QueryBuilders.boolQuery();
            List<QueryBuilder> must = bool.must();
            RangeQueryBuilder rangeStatus = QueryBuilders.rangeQuery("orderstatus").gte(1).lte(3);
            RangeQueryBuilder rangeTime = QueryBuilders.rangeQuery("createtime").gte(start).lte(end);
            must.add(rangeStatus);
            must.add(rangeTime);
            if (StrUtil.isNotBlank(category)) {
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("productcategory", category);
                must.add(termQueryBuilder);
            }
            TermsAggregationBuilder builder = AggregationBuilders.terms("province_term").field("receiverprovince.keyword").size(36);
            //嵌套分桶聚合
            TermsAggregationBuilder termSku = AggregationBuilders.terms("order_status_term").field("orderstatus").size(3);
            ValueCountAggregationBuilder field = AggregationBuilders.count("order_count").field("orderno");
            termSku.subAggregation(field);
            builder.subAggregation(termSku);
            list = ElasticsearchUtil.getOrderCategory(ElasticsearchContants.ORDER_INDEX, bool, builder);
            //从数据库中获取
//            if (CollUtil.isEmpty(list)) {
//                List<Map<String, Object>> mapList = orderMapper.getOrderProductByOrder(start, end, province);
//                for (Map<String, Object> map : mapList) {
//                    OrderCategoryDto dto = new OrderCategoryDto();
//                    dto.setProductCategory(map.get("productCategory").toString());
//                    List<OrderStatisticDto> statisticDtos = new ArrayList<>();
//                    OrderStatisticDto statisticDto = new OrderStatisticDto();
//                    statisticDto.setSkuName(map.get("skuName").toString());
//                    statisticDto.setNumsMax(Convert.toInt(map.get("numsMax")));
//                    statisticDtos.add(statisticDto);
//                    dto.setStatisticDtos(statisticDtos);
//                    list.add(dto);
//                }
//            }
        } catch (Exception e) {
            log.error("获取品牌订单统计数据异常：e->{}", e);
        }

        return list;
    }

    /**
     * 不同商品spu在不同省份的销量统计
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<OrderProvinceDto> getOrderSpuStatistic(String startTime, String endTime) {
        List<OrderProvinceDto> list = new ArrayList<>();
        try {
            Date start = DateUtils.parseDate(startTime, DateUtils.DATE_TIME_PATTERN);
            Date end = DateUtils.parseDate(endTime, DateUtils.DATE_TIME_PATTERN);
            //从es中获取
            BoolQueryBuilder bool = QueryBuilders.boolQuery();
            List<QueryBuilder> must = bool.must();
            RangeQueryBuilder rangeStatus = QueryBuilders.rangeQuery("orderstatus").gte(1).lte(3);
            RangeQueryBuilder rangeTime = QueryBuilders.rangeQuery("createtime").gte(start).lte(end);
            must.add(rangeStatus);
            must.add(rangeTime);
            TermsAggregationBuilder builder = AggregationBuilders.terms("province_term").field("receiverprovince.keyword").size(32);
            //嵌套分桶聚合
            TermsAggregationBuilder termSpu = AggregationBuilders.terms("spuname_term").field("spuname.keyword").size(200);
            SumAggregationBuilder sum = AggregationBuilders.sum("spu_nums").field("skunum");
            SumAggregationBuilder sumAmount = AggregationBuilders.sum("total_amount").field("amount");
            termSpu.subAggregation(sum).subAggregation(sumAmount);
            builder.subAggregation(termSpu);
            list = ElasticsearchUtil.getProvinceSpu(ElasticsearchContants.ORDER_INDEX, bool, builder);
            if (CollUtil.isEmpty(list)) {
                //从数据库中获取
                List<Map<String, Object>> mapList = orderMapper.getOrderSpuStatistic(start, end);
                list = getOrderSpuHandle(mapList, list);
            }
        } catch (Exception e) {
            log.error("不同商品spu在不同省份的销量统计数据异常：e->{}", e);
        }
        return list;
    }

    /**
     * 订单用户统计
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public  List<OrderUserStatisticDto> getOrderUserStatistic(String startTime, String endTime) {
        //采用多线程
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 4, 800, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024));
        //线程安全计数器
        AtomicInteger totalAtomic = new AtomicInteger();
        //采用countDownLatch
        CountDownLatch latch = new CountDownLatch(3);
        List<OrderUserStatisticDto>  dtos = new ArrayList<>();
        try {
            Date start = DateUtils.parseDate(startTime, DateUtils.DATE_TIME_PATTERN);
            Date end = DateUtils.parseDate(endTime, DateUtils.DATE_TIME_PATTERN);
            //if (ObjectUtil.isNull(dto)) {
                //总人数
                List<Map<String, Object>> mapList = getUserTotalCount(executor, totalAtomic, latch, start, end);
                //新用户
                List<Map<String, Object>> newList = getUserNewCount(executor, totalAtomic, latch, start, end);
                //老用户
                List<Map<String, Object>> oldList = getUserOldCount(executor, totalAtomic, latch, start, end);
                //子线程完成
                latch.await();
                //执行主线程
            dtos = getUserOrder(dtos, mapList, newList, oldList);
                //关闭线程池
                executor.shutdown();
           // }
        } catch (Exception e) {
            log.error("订单用户统计数据异常： e->{}", e);
        } finally {
            executor.shutdown();
        }
        return dtos;
    }

    /**
     * 统计新用户首单商品数据
     *
     * @param startTime
     * @param endTime
     * @param province
     * @param category
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public IPage<OrderProductDto> getOrderProductStatistic(String startTime, String endTime, String province, String category, Integer pageNo, Integer pageSize) {
        Page<OrderProductDto> page = new Page<>(pageNo, pageSize);
        try {
            long begin = System.currentTimeMillis();
            //从es中获取相应数据
            List<OrderProductDto> list = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            BoolQueryBuilder bool = QueryBuilders.boolQuery();
            List<QueryBuilder> must = bool.must();
            RangeQueryBuilder statusBuilder = QueryBuilders.rangeQuery("orderstatus").gte(1).lte(3);
            RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("createtime").lte(end);
            must.add(statusBuilder);
            must.add(timeBuilder);

            //进行聚合分析
            TermsAggregationBuilder builder = AggregationBuilders.terms("user_id_term").field("userid").size(2000)
                    .order(BucketOrder.aggregation("user_id_count", false))
                    .subAggregation(AggregationBuilders.count("user_id_count").field("userid"));
            //类似having的脚本
            //声明BucketPath，用于后面的bucket筛选
            Map<String, String> bucketsPathsMap = new HashMap<>(8);
            bucketsPathsMap.put("user_id_count", "user_id_count");
            Script script = new Script("params.user_id_count > 1");
            //构建bucket选择器
            BucketSelectorPipelineAggregationBuilder bs =
                    PipelineAggregatorBuilders.bucketSelector("user_id_having", bucketsPathsMap, script);
            builder.subAggregation(bs);
            List<Long> userIds = ElasticsearchUtil.getUserNames(ElasticsearchContants.ORDER_INDEX, bool, builder);
            Long total = 0L;
            if (CollUtil.isNotEmpty(userIds)) {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                List<QueryBuilder> must1 = boolQuery.must();
                RangeQueryBuilder orderstatus = QueryBuilders.rangeQuery("orderstatus").gte(1).lte(3);
                RangeQueryBuilder createtime = QueryBuilders.rangeQuery("createtime").gte(start).lte(end);
                TermsQueryBuilder terms = QueryBuilders.termsQuery("userid", userIds);
                if (StrUtil.isNotBlank(province)) {
                    TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("receiverprovince", province);
                    must1.add(termQueryBuilder);
                }
                if (StrUtil.isNotBlank(category)) {
                    TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("productcategory", category);
                    must1.add(termQueryBuilder);
                }
                must1.add(orderstatus);
                must1.add(createtime);
                must1.add(terms);
                list = ElasticsearchUtil.getOrderProduct(ElasticsearchContants.ORDER_INDEX, boolQuery, pageNo, pageSize);
                total = ElasticsearchUtil.getOrderProductCount(ElasticsearchContants.ORDER_INDEX, boolQuery);
            }

            //List<OrderNewProductDto> list = omsOrderMapper.getNewOrderPro(start,end,productBrand);
            long last = System.currentTimeMillis();
            System.out.println("用的时间：" + (last - begin));

            PageDto dto = new PageDto(total, pageSize);
            page.setRecords(list);
            page.setSize(pageSize);
            page.setPages(dto.getTotalPage());
            page.setTotal(total);
        } catch (ParseException e) {
            log.error("获取新用首单购买商品数据：e->{}", e);
        }
        return page;
    }

    @Override
    public List<ProvinceDto> getProvinceSku(String startTime, String endTime, String category) {
        List<ProvinceDto> list = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            BoolQueryBuilder bool = QueryBuilders.boolQuery();
            List<QueryBuilder> must = bool.must();
            RangeQueryBuilder statusBuilder = QueryBuilders.rangeQuery("orderstatus").gte(1).lte(3);
            RangeQueryBuilder timeBuilder = QueryBuilders.rangeQuery("createtime").lte(end);
            if (StrUtil.isNotBlank(category)) {
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("productcategory", category);
                must.add(termQueryBuilder);
            }
            must.add(statusBuilder);
            must.add(timeBuilder);
            TermsAggregationBuilder term = AggregationBuilders.terms("city_term").field("receivercity.keyword").size(3000);
            TermsAggregationBuilder builder = AggregationBuilders.terms("brand_term").field("productbrand").size(3)
                    .order(BucketOrder.aggregation("brand_count", false))
                    .subAggregation(AggregationBuilders.sum("brand_count").field("skunum"));
            term.subAggregation(builder);
            list =  ElasticsearchUtil.getProvinceBrand(ElasticsearchContants.ORDER_INDEX,bool,term,list);
        } catch (Exception e){
            log.error("各个省份下品牌销量前10的品牌异常：e->{}",e);
        }

        return list;


    }

    private List<Map<String, Object>> getUserOldCount(ThreadPoolExecutor executor, AtomicInteger totalAtomic, CountDownLatch latch, Date start, Date end) {
        List<Map<String, Object>> list = new ArrayList<>();
        executor.execute(() -> {
            List<Map<String, Object>> mapList = orderMapper.oldOrderUser(start, end);
            list.addAll(mapList);
            latch.countDown();
        });
        totalAtomic.incrementAndGet();
        return list;
    }

    private List<Map<String, Object>> getUserNewCount(ThreadPoolExecutor executor, AtomicInteger totalAtomic, CountDownLatch latch, Date start, Date end) {
        List<Map<String, Object>> list = new ArrayList<>();
        executor.execute(() -> {
            List<Map<String, Object>> mapList = orderMapper.newOrderUser(start, end);
            list.addAll(mapList);
            latch.countDown();
        });
        totalAtomic.incrementAndGet();
        return list;
    }

    private List<Map<String, Object>> getUserTotalCount(ThreadPoolExecutor executor, AtomicInteger totalAtomic, CountDownLatch latch, Date start, Date end) {
        List<Map<String, Object>> list = new ArrayList<>();
        executor.execute(() -> {
            List<Map<String, Object>> mapList = orderMapper.totalOrderUser(start, end);
            list.addAll(mapList);
            latch.countDown();
        });
        totalAtomic.incrementAndGet();
        return list;
    }

    private List<Map<String, Object>> getUserTotalNew(Date start, Date end) {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        List<QueryBuilder> must = bool.must();
        RangeQueryBuilder rangeStatus = QueryBuilders.rangeQuery("orderstatus").gte(1).lte(3);
        RangeQueryBuilder rangeTime = QueryBuilders.rangeQuery("createtime").gte(start).lte(end);
        must.add(rangeStatus);
        must.add(rangeTime);
        DateHistogramAggregationBuilder builder = AggregationBuilders.dateHistogram("user_count_day").calendarInterval(new DateHistogramInterval("day")).field("createtime");
        TermsAggregationBuilder terms = AggregationBuilders.terms("user_term").field("userid").size(1000);
        ValueCountAggregationBuilder countBuild = AggregationBuilders.count("user_id_count").field("userid");
        terms.subAggregation(countBuild);

        //声明BucketPath，用于后面的bucket筛选
        Map<String, String> bucketsPathsMap = new HashMap<>(8);
        bucketsPathsMap.put("user_id_count", "user_id_count");
        //设置脚本
        Script script = new Script("params.user_id_count < 2");
        BucketSelectorPipelineAggregationBuilder bs =
                PipelineAggregatorBuilders.bucketSelector("user_id_having", bucketsPathsMap, script);
        terms.subAggregation(bs);
        builder.subAggregation(terms);
        List<Map<String, Object>> list = ElasticsearchUtil.getTotalUserNew(ElasticsearchContants.ORDER_INDEX, bool, builder);
        return list;
    }

    private List<Map<String, Object>> getUserTotalEs(Date start, Date end) {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        List<QueryBuilder> must = bool.must();
        RangeQueryBuilder rangeStatus = QueryBuilders.rangeQuery("orderstatus").gte(1).lte(3);
        RangeQueryBuilder rangeTime = QueryBuilders.rangeQuery("createtime").gte(start).lte(end);
        must.add(rangeStatus);
        must.add(rangeTime);
        DateHistogramAggregationBuilder builder = AggregationBuilders.dateHistogram("user_count_day").calendarInterval(new DateHistogramInterval("day")).field("createtime")
                .subAggregation(AggregationBuilders.cardinality("user_total_count").field("user_id"));
        List<Map<String, Object>> list = ElasticsearchUtil.getTotalUser(ElasticsearchContants.ORDER_INDEX, bool, builder);
        return list;

    }

    private List<OrderUserStatisticDto> getUserOrder( List<OrderUserStatisticDto>  dtos, List<Map<String, Object>> mapList, List<Map<String, Object>> newList, List<Map<String, Object>> oldList) {
        Integer userTotalCount = 0;
        Integer userNewCount = 0;
        Integer userOldCount = 0;
        if (CollUtil.isNotEmpty(mapList)) {
            for (Map<String, Object> map : mapList) {

                userTotalCount = userTotalCount + (map.get("userCount") == null ? 0 : Convert.toInt(map.get("userCount")));
                Map<String, Object> newMap = newList.stream().filter(m -> ToolUtil.equals(map.get("createTime").toString(), m.get("createTime").toString())).findFirst().orElse(null);
                if (CollUtil.isNotEmpty(newMap)) {
                    userNewCount = userNewCount + (newMap.get("userCount") == null ? 0 : Convert.toInt(newMap.get("userCount")));
                }
                Map<String, Object> oldMap = oldList.stream().filter(m -> ToolUtil.equals(map.get("createTime").toString(), m.get("createTime").toString())).findFirst().orElse(null);
                if (CollUtil.isNotEmpty(oldMap)) {
                    userOldCount = userOldCount +(oldMap.get("userCount") == null ? 0 : Convert.toInt(oldMap.get("userCount")));
                }

            }
        }
        OrderUserStatisticDto dto = new OrderUserStatisticDto();
        dto.setName("总人数");
        dto.setUserCount(userTotalCount);
        OrderUserStatisticDto dto1 = new OrderUserStatisticDto();
        dto1.setName("新用户人数");
        dto1.setUserCount(userNewCount);
        OrderUserStatisticDto dto2 = new OrderUserStatisticDto();
        dto2.setName("老用户人数");
        dto2.setUserCount(userOldCount);
        dtos.add(dto2);
        dtos.add(dto1);
        dtos.add(dto);
        return dtos;
    }

    private List<OrderProvinceDto> getOrderSpuHandle(List<Map<String, Object>> mapList, List<OrderProvinceDto> list) {

        List<String> provinces = mapList.stream().map(m -> m.get("province").toString()).distinct().collect(Collectors.toList());
        for (String province : provinces) {
            OrderProvinceDto dto = new OrderProvinceDto();
            dto.setProvince(province);
            List<Map<String, Object>> collect = mapList.stream().filter(m -> ToolUtil.equals(province, m.get("province").toString())).collect(Collectors.toList());
            List<OrderSpuDto> spuDtos = new ArrayList<>();
            if (CollUtil.isNotEmpty(collect)) {
                for (Map<String, Object> map : collect) {
                    OrderSpuDto orderSpuDto = new OrderSpuDto();
                    orderSpuDto.setAmount(Convert.toBigDecimal(map.get("amount")));
                    orderSpuDto.setNums(Convert.toInt(map.get("nums")));
                    orderSpuDto.setSpuName(map.get("spuName").toString());
                    spuDtos.add(orderSpuDto);
                }
            }
            dto.setOrderSpuDtos(spuDtos);
            list.add(dto);
        }
        return list;
    }

}
