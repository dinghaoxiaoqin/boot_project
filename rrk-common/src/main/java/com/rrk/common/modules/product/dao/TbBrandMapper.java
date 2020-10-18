package com.rrk.common.modules.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.product.dto.BrandCategoryDto;
import com.rrk.common.modules.product.entity.TbBrand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系 Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
public interface TbBrandMapper extends BaseMapper<TbBrand> {

    /**
     * 获取品牌数据
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    List<BrandCategoryDto> getBrandList(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize, @Param("keyword") String keyword);

    /**
     * 获取品牌总条数
     * @param keyword
     * @return
     */
    Long getBrandCount(@Param("keyword") String keyword);
}
