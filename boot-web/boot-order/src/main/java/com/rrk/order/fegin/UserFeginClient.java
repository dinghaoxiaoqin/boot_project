package com.rrk.order.fegin;

import com.rrk.common.constant.FeginConstant;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.order.fegin.fallback.UserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程访问用户微服务
 */
@FeignClient(name = FeginConstant.USER_CLIENT_NAME,fallbackFactory = UserFallbackFactory.class)
public interface UserFeginClient {

    /**
     * 根据id获取收货人地址信息
     */
    @GetMapping(value = "/api/user/getUserAddressById")
    public TbUserAddress getUserAddressById(@RequestParam(value = "userId") Long userId,@RequestParam(value = "addressId") Long addressId);
}
