package com.rrk.gateway.service.impl;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 上面代码继承IPing接口，判断服务是否可用。
 * 我们在微服务中增加heath接口，在gateway中调用该接口，如果返回正常则认为微服务可用。
 * 健康检查
 */
@Component
public class HealthServiceImpl implements IPing {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean isAlive(Server server) {

        //调用其他服务的health（）接口判断是否健康
        String url = "http://" + server.getId() + "/health";

        try {
            ResponseEntity<String> heath = restTemplate.getForEntity(url, String.class);
            if (heath.getStatusCode() == HttpStatus.OK) {
                System.out.println("ping " + url + " success and response is " + heath.getBody());
                return true;
            }
            System.out.println("ping " + url + " error and response is " + heath.getBody());
            return false;
        } catch (Exception e) {
            System.out.println("ping " + url + " failed");
            return false;
        }

    }
}
