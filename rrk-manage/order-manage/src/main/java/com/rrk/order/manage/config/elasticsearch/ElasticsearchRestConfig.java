package com.rrk.order.manage.config.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * elasticsearch 7.6.0Rest配置信息
 */
@Configuration
@Slf4j
@Getter
@Setter
@ComponentScan(basePackageClasses = ElasticsearchConfig.class)
public class ElasticsearchRestConfig {

    @Value("${elasticSearch.client.connectNum}")
    private Integer connectNum;

    @Value("${elasticSearch.client.connectPerRoute}")
    private Integer connectPerRoute;

    @Value("${elasticSearch.hostlist}")
    private String hostlist;

    /**
     * 集群形式
     * @return
     */
    @Bean
    public HttpHost[] httpHost(){
        //解析hostlist配置信息
        String[] split = hostlist.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for(int i=0;i<split.length;i++){
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        log.info("init HttpHost");
        return httpHostArray;
    }

    @Bean(initMethod="init",destroyMethod="close")
    public ElasticsearchConfig getFactory(){
        log.info("ElasticsearchConfig 初始化");
        return ElasticsearchConfig.
                build(httpHost(), connectNum, connectPerRoute);
    }



    @Bean(name = "restHighLevelClient")
    @Scope("singleton")
    public RestHighLevelClient getRHLClient(){
        log.info("RestHighLevelClient 初始化");
        return getFactory().getRhlClient();
    }


}
