package com.rrk.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.product.dao.TbSkuMapper;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.product.service.ITbSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbSkuServiceImpl extends ServiceImpl<TbSkuMapper, TbSku> implements ITbSkuService {

}
