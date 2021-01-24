package com.rrk.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.dto.webdto.*;
import com.rrk.common.modules.product.entity.TbCart;

import java.util.List;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
public interface ITbCartService extends IService<TbCart> {

    Boolean addCart(Long userId, List<AddCartDto> addCartDtos);

    Boolean removeCart(Long userId, List<AddCartDto> addCartDtos);

    List<CartDto> getCarts(Long userId);

    SettleDto getSettleList(Long userId, SettleProVo settleProVo);
}
