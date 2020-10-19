package com.rrk.gateway.config.gatewayConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 的配置
 */
@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
}
