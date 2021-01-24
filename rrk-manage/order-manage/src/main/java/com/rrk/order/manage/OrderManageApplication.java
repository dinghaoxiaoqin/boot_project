package com.rrk.order.manage;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.rrk.common.handle.MyExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 后台订单管理启动类
 */
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
@SpringBootApplication
@EnableTransactionManagement
//动态刷新配置
@RefreshScope
@Import({MyExceptionHandler.class})
@EnableSwagger2
public class OrderManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderManageApplication.class,args);
    }
}
