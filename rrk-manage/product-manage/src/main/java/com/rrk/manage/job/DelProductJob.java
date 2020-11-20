package com.rrk.manage.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.manage.service.ITbSkuService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时删除商品sku有问题的数据
 */

@Slf4j
@JobHandler(value = "DelProductJob")
@Component
public class DelProductJob extends IJobHandler {

    @Autowired
    private ITbSkuService skuService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("------删除sku异常的数据定时任务开始执行");
        List<TbSku> list = skuService.list(new QueryWrapper<TbSku>().eq("title",""));
        if (CollUtil.isNotEmpty(list)) {
            //List<Long> ids = list.stream().filter(l -> l.getTitle() == null).map(l -> l.getId()).collect(Collectors.toList());
            for (TbSku sku : list) {
                if (StrUtil.isBlank(sku.getTitle())) {
                    skuService.remove(new QueryWrapper<TbSku>().in("id",sku.getId()));
                }

            }

        }

        log.info("-----删除sku商品的定时任务完成--------");
        return null;
    }
}
