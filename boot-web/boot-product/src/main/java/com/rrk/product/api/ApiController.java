package com.rrk.product.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.product.service.ITbSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 提供对外访问的接口
 */
@RestController
@RequestMapping("/api/product")
@CrossOrigin
@Slf4j
public class ApiController {

    @Autowired
    private ITbSkuService skuService;

    /**
     * 根据skuId获取商品sku信息
     */
    @GetMapping(value = "/getSkuById")
    public TbSku getSkuById(@RequestParam(value = "skuId") Long skuId) {
        TbSku sku = skuService.getOne(new QueryWrapper<TbSku>().eq("id", skuId));
        return sku;
    }

    /**
     * 修改库存
     */
    //事务接收方
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @PostMapping(value = "/updateStock")
    public Integer updateStock(@RequestBody TbSku sku) {
        //验证分布式事务
        // int i = 1/0;
        boolean b = skuService.updateById(sku);
        return b == true ? 1:0;
    }
}
