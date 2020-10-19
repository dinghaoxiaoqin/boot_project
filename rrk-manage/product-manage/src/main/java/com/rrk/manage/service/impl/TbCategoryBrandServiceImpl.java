package com.rrk.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.product.dao.TbCategoryBrandMapper;
import com.rrk.common.modules.product.entity.TbCategoryBrand;
import com.rrk.manage.service.ITbCategoryBrandService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品分类和品牌的中间表，两者是多对多关系 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@Service
public class TbCategoryBrandServiceImpl extends ServiceImpl<TbCategoryBrandMapper, TbCategoryBrand> implements ITbCategoryBrandService {

}
