package com.rrk.common.modules.order.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付所需的实体
 */
@Data
public class PayDto implements Serializable {

    /**
     * 订单号(必填)
     */
    private String orderNo;
    /**
     * 订单标题（必填）
     */
    private String subject;

    /**
     * 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。(非必传)
     */
    private String body;


    /**
     * 特定渠道发起时额外参数(json字符串) eg:支付宝手机网站wap支付中quit_url参数  "{"quit_url":"http://www.taobao.com/product/113714.html"}"
     * 例如:(用户付款中途退出返回商户网站的地址 eg:http://www.taobao.com/product/113714.html)
     */
    private String extra;

    /**
     * 详情参照 常量内中PayConstant 支付类型:
     */
    private String payType;

    /**
     * 买家留言信息（非必填）
     */
    private String message;
}
