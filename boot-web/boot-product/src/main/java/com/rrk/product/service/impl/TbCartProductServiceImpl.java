package com.rrk.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.product.dao.TbCartProductMapper;
import com.rrk.common.modules.product.entity.TbCartProduct;
import com.rrk.product.service.ITbCartProductService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车关联商品表 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@Service
public class TbCartProductServiceImpl extends ServiceImpl<TbCartProductMapper, TbCartProduct> implements ITbCartProductService {

}
