package com.rrk.manage.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.modules.product.entity.TbActivityProduct;
import com.rrk.common.modules.product.entity.TbPlatformActivity;
import com.rrk.manage.service.ITbActivityProductService;
import com.rrk.manage.service.ITbPlatformActivityService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动定时任务（每天0点执行）
 */
@JobHandler(value = "ActivityJob")
@Component
@Slf4j
public class ActivityJob extends IJobHandler {

    @Autowired
    private ITbActivityProductService activityProductService;

    @Autowired
    private ITbPlatformActivityService platformActivityService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("优惠活动定时任务执行了-------------------");
        List<TbPlatformActivity> activitys = platformActivityService.list(new QueryWrapper<TbPlatformActivity>().eq("is_activity", 0).isNotNull("activity_id"));
        List<TbPlatformActivity> list = new ArrayList<>();
        List<TbActivityProduct> productList = new ArrayList<>();
        if (CollUtil.isNotEmpty(activitys)) {
            for (TbPlatformActivity activity : activitys) {
                long time = activity.getOverTime().getTime() - System.currentTimeMillis();
                if (time <= 0) {
                    //超时活动结束
                    activity.setIsActivity(1);
                    list.add(activity);
                }
            }
        }
        if (CollUtil.isNotEmpty(list)) {
            platformActivityService.updateBatchById(list);
        }
        //秒杀商品结束
        List<TbActivityProduct> products = activityProductService.list(new QueryWrapper<TbActivityProduct>().eq("is_activity",0).isNull("activity_id"));
        if (CollUtil.isNotEmpty(products)) {
            for (TbActivityProduct product : products) {
                long time = product.getEndTime().getTime() - System.currentTimeMillis();
                if (time <= 0) {
                    //超时活动结束
                    product.setIsActivity(1);
                    productList.add(product);
                    //清空redis
                    redisTemplate.opsForHash().delete(RedisConstant.KILL_SKU_KEY,product.getSkuId().toString());
                }
            }
        }
        if (CollUtil.isNotEmpty(productList)) {
            activityProductService.removeByIds(productList.stream().map(p->p.getId()).collect(Collectors.toList()));
        }
        log.info("优惠活动定时任务执行了结束-------------------");
        return ReturnT.SUCCESS;
    }
}
