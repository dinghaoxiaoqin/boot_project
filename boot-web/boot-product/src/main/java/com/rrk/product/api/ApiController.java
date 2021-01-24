package com.rrk.product.api;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.modules.product.dto.webdto.SkuDto;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.product.service.ITbSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//import com.codingapi.txlcn.tc.annotation.DTXPropagation;
//import com.codingapi.txlcn.tc.annotation.LcnTransaction;

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

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 根据skuId获取商品sku信息
     */
    @GetMapping(value = "/getSkuById")
    public TbSku getSkuById(@RequestParam(value = "skuId") Long skuId) {
        TbSku sku = skuService.getOne(new QueryWrapper<TbSku>().eq("id", skuId));
        List<TbSku> list = skuService.list();
        List<Long> collect = list.stream().map(l -> l.getId()).collect(Collectors.toList());
        redisTemplate.opsForValue().set(RedisConstant.SKU_KEY, JSON.toJSONString(collect));
        String s1 = redisTemplate.opsForValue().get(RedisConstant.SKU_KEY);
        List<Long> longs = JSON.parseArray(s1, Long.class);
        System.out.println(longs);
        return sku;
    }

    /**
     * 修改库存
     */
    //事务接收方
    //@LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @PostMapping(value = "/updateStock")
    public Integer updateStock(@RequestBody TbSku sku) {
        //验证分布式事务
        // int i = 1/0;
        boolean b = skuService.updateById(sku);
        return b == true ? 1:0;
    }

    /**
     * 根据skuId 获取sku spu brand 和category信息
     */
    @GetMapping(value = "/getSkuBySkuId")
    public SkuDto getSkuBySkuId(@RequestParam(value = "skuId") Long skuId) {
        SkuDto skuDto = skuService.getSkuOne(skuId);

        return skuDto;
    }
}
