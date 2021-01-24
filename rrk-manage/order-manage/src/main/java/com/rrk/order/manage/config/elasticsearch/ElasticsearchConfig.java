package com.rrk.order.manage.config.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Arrays;

/**
 * elasticsearch7.6.0的配置信息
 * @author dinghao
 * @date 2020-08-21
 */
@Slf4j
public class ElasticsearchConfig {

    public static int CONNECT_TIMEOUT_MILLIS = 1000;
    public static int SOCKET_TIMEOUT_MILLIS = 30000;
    public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 500;
    public static int MAX_CONN_PER_ROUTE = 10;
    public static int MAX_CONN_TOTAL = 30;

    private static HttpHost[] HTTP_HOST;
    private RestClientBuilder builder;

    private RestHighLevelClient restHighLevelClient;

    private static ElasticsearchConfig elasticsearchConfig = new ElasticsearchConfig();

    private ElasticsearchConfig(){}

    public static ElasticsearchConfig build(HttpHost[] httpHostArray,
                                            Integer maxConnectNum, Integer maxConnectPerRoute){
        HTTP_HOST = httpHostArray;
        MAX_CONN_TOTAL = maxConnectNum;
        MAX_CONN_PER_ROUTE = maxConnectPerRoute;
        return  elasticsearchConfig;
    }

    public static ElasticsearchConfig build(HttpHost[] httpHostArray,Integer connectTimeOut, Integer socketTimeOut,
                                            Integer connectionRequestTime,Integer maxConnectNum, Integer maxConnectPerRoute){
        HTTP_HOST = httpHostArray;
        CONNECT_TIMEOUT_MILLIS = connectTimeOut;
        SOCKET_TIMEOUT_MILLIS = socketTimeOut;
        CONNECTION_REQUEST_TIMEOUT_MILLIS = connectionRequestTime;
        MAX_CONN_TOTAL = maxConnectNum;
        MAX_CONN_PER_ROUTE = maxConnectPerRoute;
        return  elasticsearchConfig;
    }

    /**
     * 进行初始化
     */
    public void init(){
        builder = RestClient.builder(HTTP_HOST);
        setConnectTimeOutConfig();
        setMutiConnectConfig();
        restHighLevelClient = new RestHighLevelClient(builder);
        log.info("init factory" + Arrays.toString(HTTP_HOST));
    }

    /**
     * 配置连接时间延时
     * */
    public void setConnectTimeOutConfig(){
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            return requestConfigBuilder;
        });
    }
    /**
     * 使用异步httpclient时设置并发连接数
     * */
    public void setMutiConnectConfig(){
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
            return httpClientBuilder;
        });
    }



    public RestHighLevelClient getRhlClient(){
        return restHighLevelClient;
    }

    public void close() {
        if (restHighLevelClient != null) {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("close client");
    }


}
