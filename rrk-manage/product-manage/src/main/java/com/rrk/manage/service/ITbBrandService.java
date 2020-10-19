package com.rrk.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.dto.BrandCategoryDto;
import com.rrk.common.modules.product.dto.BrandDto;
import com.rrk.common.modules.product.entity.TbBrand;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
public interface ITbBrandService extends IService<TbBrand> {
    /**
     * 获取品牌列表
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    IPage<BrandCategoryDto> getBrandList(Integer pageNo, Integer pageSize, String keyword);

    /**
     * 添加品牌
     * @param brandDto
     * @return
     */
    Integer addBrand(BrandDto brandDto);
}
