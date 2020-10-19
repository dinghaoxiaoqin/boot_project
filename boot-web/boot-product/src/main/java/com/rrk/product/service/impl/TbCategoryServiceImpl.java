package com.rrk.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.product.dao.TbCategoryMapper;
import com.rrk.common.modules.product.entity.TbCategory;
import com.rrk.product.service.ITbCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbCategoryServiceImpl extends ServiceImpl<TbCategoryMapper, TbCategory> implements ITbCategoryService {


}
