package com.rrk.order.service;

import com.rrk.common.modules.order.dto.AliNotifyResDto;
import com.rrk.common.modules.order.dto.PayDto;

public interface PayService {

    /**
     * 支付宝支付
     * @param userId
     * @param payDto
     * @return
     */
    String orderPay(Long userId, PayDto payDto);

    /**
     * 支付宝回调
     * @param aliNotifyResDto
     * @return
     */
    String payNotfiy(AliNotifyResDto aliNotifyResDto);
}
