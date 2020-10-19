package com.rrk.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.entity.TbCategory;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
public interface ITbCategoryService extends IService<TbCategory> {

}
