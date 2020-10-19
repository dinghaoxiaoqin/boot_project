package com.rrk.order.fegin;

import com.rrk.common.constant.FeginConstant;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.order.fegin.fallback.ProductFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用商品服务的数据
 */
@FeignClient(name = FeginConstant.PRODUCT_CLIENT_NAME,fallbackFactory   = ProductFallbackFactory.class)
public interface ProductFeginClient {


    /**
     * 获取商品的sku信息
     */
    @GetMapping(value = "/api/product/getSkuById")
    public TbSku getSkuById(@RequestParam(value = "skuId") Long skuId);

    /**
     * 修改库存
     */
    @PostMapping(value = "/api/product/updateStock")
    public Integer updateStock(@RequestBody TbSku sku);

}
