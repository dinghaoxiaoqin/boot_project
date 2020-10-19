package com.rrk.order.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rrk.common.R;
import com.rrk.common.constant.PayContants;
import com.rrk.common.modules.order.dto.AliNotifyResDto;
import com.rrk.common.modules.order.dto.PayDto;
import com.rrk.common.utils.DateUtils;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.order.service.PayService;
import com.rrk.order.utils.RpcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dinghao
 * @date 2020-09-14
 * 支付的前端控制器
 */
@RestController
@RequestMapping("/order")
@CrossOrigin
@Slf4j
public class PayController {

    @Autowired
    private PayService payService;



    /**
     * 下单支付
     *
     * @param request
     * @param payDto
     * @return
     */
    @PostMapping(value = "/orderPay")
    public R<Object> orderPay(HttpServletRequest request, @RequestBody PayDto payDto) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        String result = payService.orderPay(userId, payDto);
        return R.ok(200, "操作成功", result);
    }

    /**
     * 支付宝回调的接口（修改支付状态）
     */
    @RequestMapping(value = "/payCallback.htm", method = RequestMethod.POST)
    @ResponseBody
    public String payCallback(HttpServletRequest request) {
        try {
            log.info("====== 开始接收支付宝支付回调（线上订单）通知 ======");
            Map<String, String> paramsMap = RpcUtil.convertRequestParamsToMap(request);
            String data = JSON.toJSONString(paramsMap);
            log.info("支付宝回调参数:->{}", paramsMap);
            //1,获取交易状态
            String tradeStatus = paramsMap.get(PayContants.TRADE_STATUS);
            Map<String, Object> result = new HashMap<>(14);
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                //订单编号
                String orderId = paramsMap.get(PayContants.ORDER_ID);
                //支付宝交易号
                String tradeId = paramsMap.get(PayContants.TRADE_ID);
                //支付金额
                BigDecimal totalAmount = BigDecimal.valueOf(Convert.toDouble(paramsMap.get(PayContants.TOTAL_AMOUNT)));
                //支付人的编号
                String payerId = paramsMap.get(PayContants.PAYER_ID);
                //支付时间
                String payTime = paramsMap.get(PayContants.PAY_TIME);
                //支付subject
                String subject = paramsMap.get(PayContants.SUBJECT);
                String body = paramsMap.get("body");
                String orderType = paramsMap.get("orderType");
                AliNotifyResDto aliNotifyResDto = new AliNotifyResDto();
                //支付成功
                log.error("支付宝支付成功");
                result.put("orderId", orderId);
                result.put("tradeId", tradeId);
                result.put("totalAmount", totalAmount);
                result.put("payTime", payTime);
//                result.put("payType", payType);
                result.put("payerId", payerId);
                aliNotifyResDto.setOrderId(orderId);
                aliNotifyResDto.setOrderType(0);
                aliNotifyResDto.setTotalAmount(totalAmount);
                aliNotifyResDto.setPayTime(DateUtils.parseDate(payTime, DateUtils.DATE_TIME_PATTERN));
                aliNotifyResDto.setTradeId(tradeId);
                aliNotifyResDto.setPayOrderId(tradeId);
                aliNotifyResDto.setSubject(subject);

                String subcode = MapUtil.getStr(paramsMap, "sub_code");
                if (StrUtil.isBlank(subcode)) {
                    subcode = MapUtil.getStr(paramsMap, "trade_status");
                }
                aliNotifyResDto.setBuyerLogonId(MapUtil.getStr(paramsMap, "buyer_logon_id"));
                aliNotifyResDto.setSubCode(subcode);
                aliNotifyResDto.setData(data);
                //进行业务处理
                String r = payService.payNotfiy(aliNotifyResDto);
                return r;
            } else {
                log.error("支付宝回调失败: tradeStatus->{}", tradeStatus);
                return "fail";
            }
        } catch (Exception e) {
            log.error("收到支付宝回通知: e->{}", e);
            return "fail";
        } finally {
            log.info("====== 完成接收支付宝支付（线上订单）回调通知 ======");
        }
    }

}
