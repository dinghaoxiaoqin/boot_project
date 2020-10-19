package com.rrk.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.entity.TbBrand;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@Mapper
public interface ITbBrandService extends IService<TbBrand> {

}
