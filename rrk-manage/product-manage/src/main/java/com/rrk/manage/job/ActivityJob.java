package com.rrk.manage.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.modules.product.entity.TbPlatformActivity;
import com.rrk.manage.service.ITbActivityProductService;
import com.rrk.manage.service.ITbPlatformActivityService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("优惠活动定时任务执行了-------------------");
        List<TbPlatformActivity> activitys = platformActivityService.list(new QueryWrapper<TbPlatformActivity>().eq("is_activity", 0));
        List<TbPlatformActivity> list = new ArrayList<>();
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
        log.info("优惠活动定时任务执行了结束-------------------");
        return ReturnT.SUCCESS;
    }
}
