package com.rrk.order.controller;

import com.rrk.common.R;
import com.rrk.order.fegin.ProductFeginClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@CrossOrigin
@Slf4j
public class TestFeginController {

    @Autowired
    private ProductFeginClient productFeginClient;

    /**
     * 模拟支付
     * @return
     */
    @GetMapping(value = "/testPay")
    public R<Object> testPay() {

        //TbSku sku = productFeginClient.getSkuById(4483112L);

            System.out.println("测试支付");
            return R.ok(200, "操作成功","----------------testPay成功了");

    }

    /**
     * 模拟下单
     * @return
     */
    @GetMapping(value = "/testOrder")
    public R<Object> testOrder(){
       // try {
           // Thread.sleep(1000L);
            System.out.println("测试下单");
            return R.ok(200, "操作成功","----------------testOrder成功了");
       // } catch (InterruptedException e) {
         //   return null;
       // }

    }


}
