package com.rrk.product;

//import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;

import com.rrk.common.handle.MyExceptionHandler;
import com.rrk.common.handle.SentinelExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients
//@EnableDistributedTransaction
@RefreshScope  //刷新配置
@Import({MyExceptionHandler.class, SentinelExceptionHandler.class})
public class BootProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootProductApplication.class,args);

    }
}
