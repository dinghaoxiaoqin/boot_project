package com.rrk.common.modules.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.product.entity.TbCategoryBrand;

/**
 * <p>
 * 商品分类和品牌的中间表，两者是多对多关系 Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
public interface TbCategoryBrandMapper extends BaseMapper<TbCategoryBrand> {

}
