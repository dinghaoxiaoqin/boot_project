package com.rrk.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.order.dto.GeneralOrderDto;
import com.rrk.common.modules.order.dto.OrderDto;
import com.rrk.common.modules.order.dto.OrderInfo;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.order.service.ITbOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单主表 前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-09-06
 */
@RestController
@RequestMapping("/order")
@CrossOrigin
@Slf4j
public class TbOrderController {

    @Autowired
    private ITbOrderService orderService;

    /**
     * 生成订单号
     */
    @GetMapping(value = "/getOrderId")
    public R<Object> getOrderId(HttpServletRequest request){
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        String orderNo = orderService.getOrderId(userId);
        return R.ok(200,"操作成功",orderNo);
    }

    /**
     * 用户下单
     */
    @PostMapping(value = "/addOrder")
    public R<Object> addOrder(HttpServletRequest request,@RequestBody OrderDto orderDto){
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        Integer result = orderService.addOrder(orderDto,userId);
        if (result > 0) {
            return R.ok(200,"下单成功");
        }
        return R.fail(556,"下单失败");
    }

    /**
     * 订单详情
     */
    @GetMapping(value = "/getOrderByOrderNo")
    public R<Object> getOrderByOrderNo(HttpServletRequest request, @RequestParam(value = "orderNo") String orderNo){
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        GeneralOrderDto generalOrderDto = orderService.getOrderByOrderNo(orderNo);
        return R.ok(200,"操作成功",generalOrderDto);
    }

    /**
     * 订单列表
     */
    @GetMapping(value = "/getOrderInfoList")
    public R<Object> getOrderInfoList(HttpServletRequest request,
                                      @RequestParam(required = false, value = "pageSize",defaultValue = "10") Integer pageSize,
                                      @RequestParam(required = false, value = "pageNo",defaultValue = "1") Integer pageNo){
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
       // Long userId = 730578624L;
        IPage<OrderInfo> page =  orderService.getOrderInfoList(userId,pageNo,pageSize);
        return R.ok(200,"操作成功",page);
    }
}

