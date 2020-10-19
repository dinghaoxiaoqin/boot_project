package com.rrk.product.fegin.fallback;

import com.rrk.product.fegin.ProductFeginClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 调用商品微服务降级处理
 */
@Slf4j
@Component
public class ProductFallbackFactory implements ProductFeginClient {
}
