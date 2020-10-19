package com.rrk.product.fegin;

import com.rrk.common.constant.FeginConstant;
import com.rrk.product.fegin.fallback.ProductFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用商品服务的数据
 */
@FeignClient(value = FeginConstant.PRODUCT_CLIENT_NAME,fallbackFactory = ProductFallbackFactory.class)
public interface ProductFeginClient {

}
