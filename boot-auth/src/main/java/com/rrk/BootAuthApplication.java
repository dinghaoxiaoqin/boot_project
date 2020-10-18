package com.rrk;

import com.rrk.common.handle.MyExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/*****
 * @Author: dinghao
 * @Date:  2020-3-20
 * @Description:
 ****/
@SpringBootApplication
@EnableTransactionManagement
//支持动态更新
@RefreshScope
@Import({MyExceptionHandler.class})
public class BootAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootAuthApplication.class,args);
    }
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}