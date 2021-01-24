package com.rrk.common.modules.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.product.dto.webdto.ProductDto;
import com.rrk.common.modules.product.entity.TbCartProduct;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 * 购物车关联商品表 Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
public interface  TbCartProductMapper extends BaseMapper<TbCartProduct> {

    ProductDto getCartProduct(@Param("shopId") Long shopId, @Param("skuId") Long skuId);
}
