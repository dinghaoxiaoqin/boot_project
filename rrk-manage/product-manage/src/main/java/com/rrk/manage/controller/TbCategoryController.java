package com.rrk.manage.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.product.dto.CategoryDto;
import com.rrk.common.modules.product.entity.TbCategory;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.manage.service.ITbCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品类目表 前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@Api(value = "后台商品管理中心", tags = "后台商品管理中心")
@RestController
@RequestMapping("/product")
@CrossOrigin
@Slf4j
public class TbCategoryController {

    @Autowired
    private ITbCategoryService categoryService;

    /**
     * 获取商品分类列表
     *
     * @param request
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @param parentId
     * @return
     */
    @ApiOperation(value = "获取商品分类信息", httpMethod = "GET")
    @GetMapping(value = "/getCategoryList")
    public R<Object> getCategoryList(HttpServletRequest request,
                                     @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     @RequestParam(value = "keyword", required = false) String keyword,
                                     @RequestParam(value = "parentId", required = false) Long parentId) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId != null) {
            IPage<TbCategory> list = categoryService.getCategoryList(pageNo, pageSize, keyword, parentId);
            return R.ok(200, "操作成功", list);
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * 获取所有商品分类数据
     */
    @ApiOperation(value = "获取所有商品分类数据", httpMethod = "GET")
    @GetMapping(value = "/getCategorys")
    public R<Object> getCategorys(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId != null) {
            List<TbCategory> list = categoryService.list(null);
            //数据处理（改成树形结构）
            List<CategoryDto> dtos = handleList(list);
            return R.ok(200, "操作成功", dtos);
        } else {
            return R.fail(401, "请先登录");
        }
    }

    @GetMapping(value = "/getCategoryss")
    public R<Object> getCategoryss(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId != null) {
            List<TbCategory> list = categoryService.list(null);
            return R.ok(200, "操作成功", list);
        } else {
            return R.fail(401, "请先登录");
        }
    }


    /**
     * 分类数据采用树形数据返回
     *
     * @param list
     * @return
     */
    private List<CategoryDto> handleList(List<TbCategory> list) {
        List<CategoryDto> dtos = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            for (TbCategory tbCategory : list) {
                if (tbCategory.getParentId().longValue() == 0) {
                    CategoryDto categoryDto = new CategoryDto(tbCategory);
                    dtos.add(categoryDto);
                }
            }
            dtos = addData(list, dtos);
        }
        return dtos;
    }

    private List<CategoryDto> addData(List<TbCategory> list, List<CategoryDto> dtos) {
        if (CollUtil.isNotEmpty(dtos)) {
            for (CategoryDto dto : dtos) {
                List<CategoryDto> categoryDtos = new ArrayList<>();
                List<TbCategory> categoryList = list.stream().filter(c -> dto.getId().longValue() == c.getParentId().longValue()).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(categoryList)) {
                    for (TbCategory tbCategory : categoryList) {
                        CategoryDto categoryDto = new CategoryDto(tbCategory);
                        categoryDtos.add(categoryDto);
                    }
                }
                dto.setChildList(categoryDtos);
                addData(list, categoryDtos);
            }
        }
        return dtos;
    }

    /**
     * 添加商品分类
     */
    @PostMapping(value = "/addCategory")
    public R<Object> addCategory(HttpServletRequest request, @RequestBody TbCategory category) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId != null) {
            TbCategory serviceOne = categoryService.getOne(new QueryWrapper<TbCategory>().eq("id", category.getId()));
            if (ObjectUtil.isNotNull(serviceOne)) {
                //修改
                boolean b = categoryService.updateById(category);
                if (b) {
                    return R.ok(200, "修改成功", "");
                }
                return R.fail(501, "修改商品分类失败");
            }

            boolean save = categoryService.save(category);
            if (save) {
                return R.ok(200, "添加成功", "");
            }
            return R.fail(501, "添加商品分类失败");
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * 根据商品id获取商品分类数据
     */
    @GetMapping(value = "/getProductCateId")
    public R<Object> getProductCateId(HttpServletRequest request,
                                      @RequestParam(value = "categoryId") Long categoryId) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId != null) {
            TbCategory serviceOne = categoryService.getOne(new QueryWrapper<TbCategory>().eq("id", categoryId));
            return R.ok(200, "", serviceOne);
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * 删除商品分类
     */
    @PostMapping(value = "/delCategory")
    public R<Object> delCategory(HttpServletRequest request,
                                 @RequestParam(value = "id") Long id) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId != null) {
            boolean b = categoryService.removeById(id);
            if (b) {
                return R.ok(200, "删除成功");
            }
            return R.fail(501, "删除失败");
        } else {
            return R.fail(401, "请先登录");
        }

    }

    /**
     * 获取商品分类数据
     */
    @GetMapping(value = "/getProCategorys")
    public R<Object> getProCategorys(@RequestParam(value = "ids") String ids) {
        if (!ids.contains("&")) {
            String[] split = ids.split("&");
            List<String> list1 = Arrays.asList(split);
            List<Long> collect = list1.stream().map(l -> Convert.toLong(l)).collect(Collectors.toList());
            List<TbCategory> list = categoryService.list(new QueryWrapper<TbCategory>().in("parent_id", collect));
            return R.ok(200, "操作成功", list);
        } else {
            String[] split = ids.split("&");
            List<String> strList = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                String substring = split[i].substring(split[i].indexOf("=")+1);
                strList.add(substring);
            }
            List<Long> collect = strList.stream().map(l -> Convert.toLong(l)).collect(Collectors.toList());
            List<TbCategory> list = categoryService.list(new QueryWrapper<TbCategory>().in("parent_id", collect));
            return R.ok(200, "操作成功", list);
        }
    }


}

