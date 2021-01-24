package com.rrk.common.modules.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.product.entity.TbPlatformActivity;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
public interface TbPlatformActivityMapper extends BaseMapper<TbPlatformActivity> {

    TbPlatformActivity getById(@Param("id") Long id);
}
