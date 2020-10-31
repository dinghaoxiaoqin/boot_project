package com.rrk.order;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.rrk.common.handle.MyExceptionHandler;
import com.rrk.common.handle.SentinelExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import com.rrk.common.handle.SentinelExceptionHandler;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RefreshScope
@EnableTransactionManagement
@EnableDistributedTransaction
@Import({MyExceptionHandler.class, SentinelExceptionHandler.class})
public class BootOrderApplication {

    public static void main(String[] args) {

        SpringApplication.run(BootOrderApplication.class,args);
    }

//    @Value("${spring.datasource.druid.url}")
//    private String config;
//
//    @GetMapping(value = "/getValue")
//    public String getValue(){
//        return config;
//    }
}
