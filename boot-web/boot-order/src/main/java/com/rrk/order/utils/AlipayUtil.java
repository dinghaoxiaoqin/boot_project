package com.rrk.order.utils;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.rrk.order.config.payConfig.AliPayConfig;

public class AlipayUtil {

    public static String connect(AlipayBean alipayBean) throws AlipayApiException {
        //1、获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                AliPayConfig.GATEWAY_URL,
                AliPayConfig.APP_ID,
                AliPayConfig.PRIVATE_KEY,
                AliPayConfig.FORMAT,
                AliPayConfig.CHARSET,
                AliPayConfig.PUBLIC_KEY,
                AliPayConfig.SIGN_TYPE
        );
        //2、设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //页面跳转同步通知页面路径
        alipayRequest.setReturnUrl(AliPayConfig.NotifyUrl);
        // 服务器异步通知页面路径
        alipayRequest.setNotifyUrl(AliPayConfig.NotifyUrl);
        //封装参数
        alipayRequest.setBizContent(JSON.toJSONString(alipayBean));

        //3、请求支付宝进行付款，并获取支付结果
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //返回付款信息
        return result;
    }
}
