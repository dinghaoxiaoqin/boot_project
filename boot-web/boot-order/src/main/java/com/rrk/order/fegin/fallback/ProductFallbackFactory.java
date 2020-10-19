package com.rrk.order.fegin.fallback;

import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.rrk.common.constant.FeginConstant;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.order.fegin.ProductFeginClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 调用商品微服务降级处理
 */
@Slf4j
@Component
public class ProductFallbackFactory implements FallbackFactory<ProductFeginClient> {

//    /**
//     * 获取商品sku服务降级
//     * @param skuId
//     * @return
//     */
//    @Override
//    public TbSku getSkuById(Long skuId) {
//        log.info("获取商品微服务->{}的->{}服务降级", FeginConstant.PRODUCT_CLIENT_NAME,"getSkuById");
//        return null;
//    }
//
//    /**
//     * 修改sku的库存服务降级
//     * @param sku
//     * @return
//     */
//    @Override
//    public Integer updateStock(TbSku sku) {
//        log.info("商品服务->{}的->{}接口降级",FeginConstant.PRODUCT_CLIENT_NAME,"updateStock");
//        return null;
//    }

    @Override
    public ProductFeginClient create(Throwable throwable) {
        return new ProductFeginClient() {
            @Override
            public TbSku getSkuById(Long skuId) {
                TbSku sku = new TbSku();
                if (throwable instanceof FlowException) {
                    log.error("商品服务->{}的->{}流控了---->{}", FeginConstant.PRODUCT_CLIENT_NAME,"getSkuById",throwable.getMessage());
                    sku.setId(-1L);
                }else {
                    log.error("商品服务->{}的->{}接口降级---->{}", FeginConstant.PRODUCT_CLIENT_NAME,"getSkuById",throwable.getMessage());
                    sku.setId(-2L);
                }
                return sku;
            }

            @Override
            public Integer updateStock(TbSku sku) {
                Integer result = 0;
                if (throwable instanceof FlowException) {
                    log.error("商品服务->{}的->{}流控了---->{}", FeginConstant.PRODUCT_CLIENT_NAME,"updateStock",throwable.getMessage());
                    result = -1;
                }else {
                    log.error("商品服务->{}的->{}接口降级---->{}", FeginConstant.PRODUCT_CLIENT_NAME,"updateStock",throwable.getMessage());
                    result = -2;
                }
                return result;
            }
        };
    }
}
