package com.rrk;

import com.rrk.common.handle.MyExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
@Import({MyExceptionHandler.class})
public class BootGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootGatewayApplication.class, args);
    }


    /**
     * 采用创建用户唯一标识，使用IP作为用户唯一标识
     * 来根据IP进行限流操作
     */
    @Bean(name = "ipKeyResolver")
    public KeyResolver userKeyResolver(){
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //通过用户身份识别唯一标识【ip】
                final String ip = exchange.getRequest().getRemoteAddress().getHostString();
                System.out.println("用户请求的ip:"+ip);
                return Mono.just(ip);
            }
        };
    }
}
