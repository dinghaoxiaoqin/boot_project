package com.rrk.manage;

import com.rrk.common.handle.MyExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@RefreshScope //加载配置
@Import({MyExceptionHandler.class})
public class EmployeeManageApplication {

    public static void main(String[] args) {

        SpringApplication.run(EmployeeManageApplication.class,args);
    }
}
