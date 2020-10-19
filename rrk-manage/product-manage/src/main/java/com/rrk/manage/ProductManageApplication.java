package com.rrk.manage;

import com.rrk.common.handle.MyExceptionHandler;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableSwagger2Doc
@RefreshScope //动态刷新配置
@Import({MyExceptionHandler.class})
public class ProductManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductManageApplication.class,args);
    }
}
