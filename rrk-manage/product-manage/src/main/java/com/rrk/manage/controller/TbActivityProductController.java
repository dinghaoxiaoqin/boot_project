package com.rrk.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.product.entity.TbActivityProduct;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.manage.dto.ActivityProductDto;
import com.rrk.manage.service.ITbActivityProductService;
import com.rrk.manage.service.ITbSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品参与活动前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@RestController
@RequestMapping("/activity")
@Slf4j
@CrossOrigin
public class TbActivityProductController {

    @Autowired
    private ITbSkuService skuService;

    @Autowired
    private ITbActivityProductService activityProductService;

    /**
     * 获取所有商品列表
     */
    @GetMapping(value = "/getSkuList")
    public R<Object> getSkuList() {
        List<TbSku> sk = skuService.list(new QueryWrapper<TbSku>().eq("enable", 1));
        return R.ok(200, "操作成功", sk);
    }

    /**
     * 添加活动商品
     */
    @PostMapping(value = "/addActivityProduct")
    public R<Object> addActivityProduct(@RequestBody ActivityProductDto activityProductDto) {

        boolean flag = activityProductService.addActivityProduct(activityProductDto);
        return flag == true ? R.ok(200, "参加活动成功", "") : R.fail(400, "参加是失败" );
    }

    /**
     * 获取活动商品列表
     */
    @GetMapping(value = "/getActivityProducts")
    public R<Object> getActivityProducts(@RequestParam(value = "pageNo") Integer pageNo,
                                         @RequestParam(value = "pageSize") Integer pageSize,
                                         @RequestParam(value = "keyword",required = false) String keyword,
                                         @RequestParam(value = "id") Integer id){
        IPage<TbActivityProduct> page = activityProductService.getActivityProducts(pageNo,pageSize,keyword,id);
        return R.ok(200,"操作成功",page);
    }

}

