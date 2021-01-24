package com.rrk.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.entity.TbActivityProduct;
import com.rrk.manage.dto.ActivityProductDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
public interface ITbActivityProductService extends IService<TbActivityProduct> {

    boolean addActivityProduct(ActivityProductDto activityProductDto);

    IPage<TbActivityProduct> getActivityProducts(Integer pageNo, Integer pageSize, String keyword, Integer id);
}
