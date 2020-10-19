package com.rrk.manage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.product.dao.TbCategoryMapper;
import com.rrk.common.modules.product.entity.TbCategory;
import com.rrk.manage.service.ITbCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ITbCategoryService categoryService;

    /**
     * 获取商品分类列表
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param parentId
     * @return
     */
    @Override
    public IPage<TbCategory> getCategoryList(Integer pageNo, Integer pageSize, String keyword, Long parentId) {
        log.info("商品列表传入的参数：pageNo->{},pageSize->{},keyword->{}，parentId->{}", pageNo, pageSize, keyword, parentId);
        Page<TbCategory> page = new Page<>(pageNo, pageSize);
        if (StrUtil.isBlank(keyword)) {
            IPage<TbCategory> categoryIPage = categoryService.page(page, new QueryWrapper<TbCategory>().eq("parent_id", parentId));
            return categoryIPage;
        } else {
            return categoryService.page(page, new QueryWrapper<TbCategory>().like("name", keyword));
        }
    }
}
