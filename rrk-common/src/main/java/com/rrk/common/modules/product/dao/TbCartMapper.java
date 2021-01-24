package com.rrk.common.modules.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.product.dto.webdto.CartDto;
import com.rrk.common.modules.product.entity.TbCart;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
public interface TbCartMapper extends BaseMapper<TbCart> {

    /**
     *
     * @param userId
     * @return
     */
    List<CartDto> getCarts(@Param("userId") Long userId);
}
