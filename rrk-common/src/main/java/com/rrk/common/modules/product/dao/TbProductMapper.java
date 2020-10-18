package com.rrk.common.modules.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.product.entity.TbProduct;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-03-05
 */
public interface TbProductMapper extends BaseMapper<TbProduct> {

    List<Map<String, Object>> selectPro();

    int updateProductById(@Param("productId") Long productId, @Param("num") Integer num);

    TbProduct getPro(@Param("productId") Long productId);
}
