package com.rrk.order.controller;

import com.rrk.common.R;
import com.rrk.common.modules.order.dto.KillOrderDto;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.order.service.KillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 秒杀订单前端控制器
 */
@RestController
@RequestMapping("/order")
@CrossOrigin
@Slf4j
public class KillOrderController {

    @Autowired
    private KillOrderService killOrderService;

    @PostMapping(value = "/addKillOrder")
    public R<Object> addKillOrder(HttpServletRequest request, @RequestBody KillOrderDto killOrderDto) {
        Long userId = JwtTokenUtil.getUserId(request);
        Integer result = killOrderService.addKillOrder(userId, killOrderDto.getSkuId());
        if (result == 7) {
            return R.ok(200, "下单成功", "");
        } else if (result == 3) {
            return R.ok(200, "系统繁忙，请稍后再试", "");
        } else if (result == 4) {
            return R.ok(200, "该商品暂没货", "");
        } else if (result == 5) {
            return R.ok(200, "该商品已售罄", "");
        } else {
            return R.fail(400, "下单失败，请联系客服");
        }

    }
}
