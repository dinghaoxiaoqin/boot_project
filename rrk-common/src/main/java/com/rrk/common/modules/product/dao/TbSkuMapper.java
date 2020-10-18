package com.rrk.common.modules.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.product.dto.webdto.ProductDetailDto;
import com.rrk.common.modules.product.entity.TbSku;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8 Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
public interface TbSkuMapper extends BaseMapper<TbSku> {

    /**
     * 获取商品详情
     * @param skuId
     * @return
     */
    ProductDetailDto getProductDetail(@Param("skuId") Long skuId);
}
