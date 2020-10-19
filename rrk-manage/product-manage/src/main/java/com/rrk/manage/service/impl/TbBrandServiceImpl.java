package com.rrk.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.dto.PageDto;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.product.dao.TbBrandMapper;
import com.rrk.common.modules.product.dto.BrandCategoryDto;
import com.rrk.common.modules.product.dto.BrandDto;
import com.rrk.common.modules.product.entity.TbBrand;
import com.rrk.common.modules.product.entity.TbCategoryBrand;
import com.rrk.manage.service.ITbBrandService;
import com.rrk.manage.service.ITbCategoryBrandService;
import com.rrk.manage.service.ITbCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ITbBrandService brandService;

    @Autowired
    private ITbCategoryService categoryService;

    @Autowired
    private ITbCategoryBrandService categoryBrandService;
    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * 获取品牌分类列表
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    @Override
    public IPage<BrandCategoryDto> getBrandList(Integer pageNo, Integer pageSize, String keyword) {
        log.info("传入的参数：pageNo->{},pageSize->{},keyword->", pageNo, pageSize, keyword);
        Page<BrandCategoryDto> page = new Page<>(pageNo, pageSize);
        PageDto pageDto = new PageDto(pageNo, pageSize);
        List<BrandCategoryDto> list = brandMapper.getBrandList(pageDto.getPage(), pageDto.getPageSize(), keyword);
        //获取总条数
        Long total = brandMapper.getBrandCount(keyword);
        page.setRecords(list);
        page.setTotal(total);
        page.setPages(new PageDto(total, pageSize).getTotalPage());
        return page;
    }

    /**
     * 添加品牌
     *
     * @param brandDto
     * @return
     */
    @Override
    public Integer addBrand(BrandDto brandDto) {
        if (ObjectUtil.isNotNull(brandDto)) {
             TbBrand tbBrand = new TbBrand(brandDto);
             boolean save = brandService.save(tbBrand);
            if (save) {
                List<TbCategoryBrand> list = new ArrayList<>();
                TbCategoryBrand categoryBrand = new TbCategoryBrand(tbBrand.getId(),brandDto.getCategoryId());
                list.add(categoryBrand);
                if (CollUtil.isNotEmpty(brandDto.getPidIds())) {
                    for (Long categoryId : brandDto.getPidIds()) {
                        TbCategoryBrand tbCategoryBrand = new TbCategoryBrand(tbBrand.getId(),categoryId);
                        list.add(tbCategoryBrand);
                    }
                }
                if (CollUtil.isNotEmpty(brandDto.getParIds())) {
                    for (Long categoryId : brandDto.getParIds()) {
                        TbCategoryBrand tbCategoryBrand = new TbCategoryBrand(tbBrand.getId(),categoryId);
                        list.add(tbCategoryBrand);
                    }
                }

                if (CollUtil.isNotEmpty(list)) {
                     boolean b = categoryBrandService.saveBatch(list);
                     return b? 1:0;
                }
            } else {
                return 0;
            }
        } else {
            throw new MyException("未找到数据");
        }
        return 0;
    }
}
