package com.rrk.manage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.product.dao.TbActivityProductMapper;
import com.rrk.common.modules.product.entity.TbActivityProduct;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.manage.dto.ActivityProductDto;
import com.rrk.manage.service.ITbActivityProductService;
import com.rrk.manage.service.ITbSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@Service
@Slf4j
@Transactional(rollbackFor = MyException.class)
public class TbActivityProductServiceImpl extends ServiceImpl<TbActivityProductMapper, TbActivityProduct> implements ITbActivityProductService {

    @Autowired
    private ITbSkuService skuService;

    @Autowired
    private ITbActivityProductService activityProductService;

    /**
     * 商品参与活动
     *
     * @param activityProductDto
     * @return
     */
    @Override
    public boolean addActivityProduct(ActivityProductDto activityProductDto) {
        log.info("商品参与活动传入参数：activityProductDto->{}", activityProductDto);
        TbSku tbSku = skuService.getById(activityProductDto.getSkuId());
        TbActivityProduct byId = activityProductService.getById(activityProductDto.getId());
        if (ObjectUtil.isNotNull(byId)) {
            byId = getActivityPro(byId, tbSku, activityProductDto);
            //修改
            byId.setUpdateTime(new Date());
            return activityProductService.updateById(byId);
        } else {
            //新增
            byId = new TbActivityProduct();
            byId = getActivityPro(byId, tbSku, activityProductDto);
            byId.setCreateTime(new Date());
            return activityProductService.save(byId);
        }
    }

    private TbActivityProduct getActivityPro(TbActivityProduct byId, TbSku tbSku, ActivityProductDto activityProductDto) {
        byId.setActivityId(activityProductDto.getActivityId());
        byId.setActivityPrice(activityProductDto.getActivityPrice());
        byId.setActivityStock(activityProductDto.getActivityStock());
        byId.setIsSell(activityProductDto.getIsSell());
        byId.setSkuDesc(tbSku.getOwnSpec());
        byId.setSkuId(activityProductDto.getSkuId());
        byId.setSpuId(tbSku.getSpuId());
        byId.setSkuImage(tbSku.getImages());
        byId.setSkuName(tbSku.getTitle());
        return byId;
    }

    /**
     * 获取活动商品列表
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param id
     * @return
     */
    @Override
    public IPage<TbActivityProduct> getActivityProducts(Integer pageNo, Integer pageSize, String keyword, Integer id) {
        log.info("活动商品传入的参数：pageNo->{},pageSize->{},keyword->{},id->{}", pageNo, pageSize, keyword, id);
        if (StrUtil.isBlank(keyword)) {
            return activityProductService.page(new Page<>(pageNo, pageSize), new QueryWrapper<TbActivityProduct>().eq("activity_id", id));
        } else {
           return  activityProductService.page(new Page<>(pageNo,pageSize),new QueryWrapper<TbActivityProduct>().eq("activity_id",id).like("sku_name",keyword));
        }

    }
}
