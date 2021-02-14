package com.rrk.order.manage.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.common.modules.user.entity.TbRegion;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.order.manage.dto.CloseOrderDto;
import com.rrk.order.manage.dto.OrderDetailDto;
import com.rrk.order.manage.dto.OrderFastDto;
import com.rrk.order.manage.dto.TraceDto;
import com.rrk.order.manage.service.ITbOrderService;
import com.rrk.order.manage.service.ITbRegionService;
import com.rrk.order.manage.service.ITbUserAddressService;
import com.rrk.order.manage.service.ITbUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台订单管理前端控制器
 */
@RestController
@Slf4j
@RequestMapping(value = "/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private ITbOrderService orderService;

    @Autowired
    private ITbUserService userService;

    @Autowired
    private ITbRegionService regionService;

    @Autowired
    private ITbUserAddressService userAddressService;

    /**
     * 获取订单列表数据
     *
     * @param pageNo
     * @param pageSize
     * @param orderStatus
     * @param startTime
     * @param endTime
     * @param orderNo
     * @return
     */
    @GetMapping(value = "/getOrderList")
    public R<Object> getOrderList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "orderStatus", required = false) Integer orderStatus,
                                  @RequestParam(value = "startTime", required = false) String startTime,
                                  @RequestParam(value = "endTime", required = false) String endTime,
                                  @RequestParam(value = "orderNo", required = false) String orderNo) {
        IPage<TbOrder> list = orderService.getOrderList(pageNo, pageSize, orderStatus, startTime, endTime, orderNo);
        return R.ok(200, "操作成功", list);
    }

    /**
     * 获取要发货的订单数据
     */
    @GetMapping(value = "/getDelieverOrder")
    public R<Object> getDelieverOrder(@RequestParam(value = "ids") String ids) {
        log.info("获取订单传入的参数：ids->{}", ids);
        //1获取订单数据
        String[] split = ids.split(",");
        List<String> strings = Arrays.asList(split);
        List<Long> collect = strings.stream().map(s -> Convert.toLong(s)).collect(Collectors.toList());
        List<TbOrder> orders = orderService.list(new QueryWrapper<TbOrder>().eq("order_status", 1).in("id", collect));
        if (CollUtil.isEmpty(orders)) {
            throw new MyException("该订单不存在，请联系管理员");
        }
        List<OrderFastDto> list = new ArrayList<>();
        //2，获取发货人的数据
        for (TbOrder order : orders) {
            TbUser user =  userService.findUserByUserId(order.getUserId());
            //3,获取发货人的地址
            TbUserAddress userAddress = userAddressService.getAddressById(order.getAddressId());

            OrderFastDto orderFastDto = new OrderFastDto(order, user, userAddress);
            list.add(orderFastDto);
        }

        // OrderFastDto orderFast = orderService.getDelieverOrder(id);
        return R.ok(200, "操作成功", list);
    }

    /**
     * 订单发货
     */
    @PostMapping(value = "/deliveryOrder")
    public R<Object> deliveryOrder(HttpServletRequest request, @RequestBody List<OrderFastDto> list) {
        Long userId = JwtTokenUtil.getUserId(request);
        Integer count = orderService.deliveryOrder(list, userId);

        if (count == 1) {
            return R.ok(200, "发货成功", "");
        }
        return R.fail(400, "发货成功");
    }

    /**
     * 获取订单详情数据
     */
    @GetMapping(value = "/getOrderDetail")
    public R<Object> getOrderDetail(@RequestParam(value = "orderId") Long orderId) {
        OrderDetailDto detailDto = orderService.getOrderDetail(orderId);
        TbUser user =  userService.findUserByUserId(detailDto.getUserId());
        //3,获取发货人的地址
        TbUserAddress userAddress = userAddressService.getAddressById(detailDto.getAddressId());
        detailDto.setReceiverAddress(userAddress.getUserAddress());
        detailDto.setReceiverName(user.getNickName());
        detailDto.setReceiverPhone(user.getPhone());
        return R.ok(200, "操作成功", detailDto);
    }

    /**
     * 获取跟踪物流的数据
     */
    @GetMapping(value = "/getOrderFast")
    public R<Object> getOrderFast(@RequestParam(value = "orderNo") String orderNo) {
        List<TraceDto> list = orderService.getOrderFast(orderNo);
        return R.ok(200, "操作成功", list);
    }

    /**
     * 添加订单备注
     */
    @PostMapping(value = "/closeOrder")
    public R<Object> closeOrder(HttpServletRequest request, @RequestBody CloseOrderDto closeOrderDto) {
        Long userId = JwtTokenUtil.getUserId(request);
        Boolean flag = orderService.closeOrder(closeOrderDto,userId);
        return flag == true ? R.ok(200, "添加备注成功", "") : R.fail(400, "添加备注失败");
    }
    /**
     * 删除订单
     */
    @PostMapping(value = "/deleteOrder")
    public R<Object> deleteOrder(HttpServletRequest request,@RequestParam(value = "ids") List<Long> ids){
        Long userId = JwtTokenUtil.getUserId(request);
        Boolean flag = orderService.deleteOrder(ids,userId);
        return flag == true ? R.ok(200, "删除成功", "") : R.fail(400, "删除失败");

    }

    /**
     *关闭订单
     */
    @PostMapping(value = "/orderClose")
    public R<Object> orderClose(HttpServletRequest request,@RequestParam(value = "orderId") Long orderId){
        Long userId = JwtTokenUtil.getUserId(request);
        Boolean flag = orderService.orderClose(orderId,userId);
        return flag == true ? R.ok(200, "关闭成功", "") : R.fail(400, "关闭失败");
    }

    /**
     * 获取省份列表
     */
     @GetMapping(value = "/getProvinceList")
    public R<Object> getProvinceList(){
         List<TbRegion> list =  regionService.findProvinceList();
        // List<TbRegion> list = regionService.list(new QueryWrapper<TbRegion>().eq("level", 1));
         //TbUser user = userService.getOne(new QueryWrapper<TbUser>().eq("user_id", detailDto.getUserId()));

         return R.ok(200,"操作成功",list);
     }
}
