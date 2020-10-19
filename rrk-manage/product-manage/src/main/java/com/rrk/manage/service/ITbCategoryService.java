package com.rrk.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.entity.TbCategory;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
public interface ITbCategoryService extends IService<TbCategory> {

    /**
     * 获取商品分类列表
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param parentId
     * @return
     */
    IPage<TbCategory> getCategoryList(Integer pageNo, Integer pageSize, String keyword, Long parentId);
}
