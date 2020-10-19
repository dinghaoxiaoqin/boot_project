package com.rrk.gateway.config.gatewayConfig;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义负载均衡规则（每个微服务访问 5次）
 */
public class MyRobbinRole extends AbstractLoadBalancerRule {

    //统计访问服务的次数
    private volatile Integer total = 0;

    //初始
    private volatile Integer index = 0;

    List<Server> upList = new ArrayList<>();

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        return choose(this.getLoadBalancer(), key);
    }

    private Server choose(ILoadBalancer loadBalancer, Object key) {

        //首先判断加载器是否为空
        if (loadBalancer == null) {
            return null;
        } else {
            Server server = null;
            while (server == null) {
                if (Thread.interrupted()) {
                    return null;
                }
                List<Server> allList = loadBalancer.getAllServers();
                int serverCount = allList.size();
                if (serverCount == 0) {
                    return null;
                }

                if (total == 0) {
                    upList = loadBalancer.getReachableServers();
                }

                if (total < 6) {
                    if (upList.size() != loadBalancer.getReachableServers().size()) {
                        index = 0;
                    }
                    server = loadBalancer.getReachableServers().get(index);
                    total++;
                } else {
                    total = 0;
                    index++;
                    if (index >= loadBalancer.getReachableServers().size()) {
                        index = 0;
                    }
                }

                if (server == null) {
                    Thread.yield();
                } else {
                    if (server.isAlive()) {
                        return server;
                    }

                    server = null;
                    Thread.yield();
                }
            }
            return server;

        }
    }

}
