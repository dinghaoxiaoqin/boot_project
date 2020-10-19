package com.rrk.order.utils;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AlipayBean {
    /*商户订单号，必填*/
    private String out_trade_no;
    /*订单名称，必填*/
    private String subject;
    /*付款金额，必填*/
    private StringBuffer total_amount;
    /*商品描述，可空*/
    private String body;
    /*超时时间参数*/
    private String timeout_express = "10m";
    //QUICK_WAP_WAY   FAST_INSTANT_TRADE_PAY
    private String product_code = "QUICK_WAP_WAY";

}