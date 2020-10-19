package com.rrk.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.product.dao.TbBrandMapper;
import com.rrk.common.modules.product.entity.TbBrand;
import com.rrk.product.service.ITbBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TbBrandServiceImpl extends ServiceImpl<TbBrandMapper, TbBrand> implements ITbBrandService {

}
