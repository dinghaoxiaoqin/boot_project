package com.rrk.user;

import com.rrk.common.handle.MyExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope //动态更新配置文件
@EnableTransactionManagement
@Import({MyExceptionHandler.class})
public class BootUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootUserApplication.class,args);
    }
}
