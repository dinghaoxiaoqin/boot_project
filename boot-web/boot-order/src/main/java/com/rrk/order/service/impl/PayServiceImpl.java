package com.rrk.order.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.handle.OrderException;
import com.rrk.common.modules.order.dto.AliNotifyResDto;
import com.rrk.common.modules.order.dto.PayDto;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.order.config.payConfig.AliPayConfig;
import com.rrk.order.service.ITbOrderService;
import com.rrk.order.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dinghao
 * @date 2020-09-14
 * 支付的业务层处理
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private ITbOrderService orderService;

    /**
     * 订单支付（手机端H5支付）
     *
     * @param userId
     * @param payDto
     * @return
     */
    @Override
    public String orderPay(Long userId, PayDto payDto) {
        return alipayTradeAppPay(payDto);
    }

    private String alipayTradeAppPay(PayDto payDto) {
        TbOrder orderNo = orderService.getOne(new QueryWrapper<TbOrder>().eq("order_no", payDto.getOrderNo()));
        if (ObjectUtil.isNull(orderNo)) {
            throw new OrderException(566, "要支付的订单不存在");
        }
        Map<String, String> body = new HashMap<>(2);
        body.put("name", payDto.getSubject());
//        AlipayBean alipayBean = new AlipayBean()
//                .setBody("测试")
//                .setOut_trade_no(payDto.getOrderNo())
//                .setTotal_amount(new StringBuffer().append(orderNo.getAmount().toString()))
//                .setSubject(payDto.getSubject());

        AlipayTradeWapPayRequest aRequest = new AlipayTradeWapPayRequest();
        AlipayTradeAppPayModel amodel = new AlipayTradeAppPayModel();
//
        amodel.setSubject(payDto.getSubject());
        amodel.setBody(payDto.getBody());
        //  amodel.setGoodsType(AliPayConfig.GOODSTYPE);
        //amodel.setGoodsType("1");
        amodel.setOutTradeNo(payDto.getOrderNo());
        amodel.setProductCode("QUICK_WAP_WAY");
        //获取支付金额
        amodel.setTotalAmount(orderNo.getAmount().toString());
        //支付宝支付的时间设置
        amodel.setTimeoutExpress("30m");
        aRequest.setBizModel(amodel);
        try {
            aRequest.setNotifyUrl(AliPayConfig.NotifyUrl);
            aRequest.setReturnUrl(AliPayConfig.NotifyUrl);
            //  String responseBody = AlipayUtil.connect(alipayBean);
            DefaultAlipayClient alipayClient = AliPayConfig.getAlipayClient();
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(aRequest);
            log.info("获取支付宝返回的数据：->{}", response.getBody());
            return response.getBody();
        } catch (AlipayApiException e) {
            throw new OrderException(556, "支付宝支付异常");
        }
    }

    /**
     * 支付宝回调业务处理
     *
     * @param aliNotifyResDto
     * @return
     */
    @Override
    public String payNotfiy(AliNotifyResDto aliNotifyResDto) {
        log.info("=============支付宝支付回调通知业务处理开始======================");
        try {
            //1参数校验
            Assert.notNull(aliNotifyResDto.getSubCode(), "支付结果不能为空");
            Assert.notNull(aliNotifyResDto.getOrderId(), "定单编号不能为空");
            // Assert.notNull(aliNotifyResDto.getOrderType(), "定单类型不能为空");
            Assert.notNull(aliNotifyResDto.getTotalAmount(), "金额不能为空");
            Assert.notNull(aliNotifyResDto.getPayTime(), "支付时间不能为空");
            //todo:2订单业务处理(后期需要进行优化)
            Boolean flag = orderService.updateOrderStaus(aliNotifyResDto);
            return flag == true ? "success" : "fail";
        } catch (Exception e) {
            log.error("异常消息通知,aliNotifyResDto:{}", aliNotifyResDto, e);
            return "fail";
        }
    }

}
