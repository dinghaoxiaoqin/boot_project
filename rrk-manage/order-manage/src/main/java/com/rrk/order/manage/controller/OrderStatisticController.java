package com.rrk.order.manage.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.dto.OrderCategoryDto;
import com.rrk.common.dto.OrderProductDto;
import com.rrk.common.dto.OrderProvinceDto;
import com.rrk.common.dto.OrderUserStatisticDto;
import com.rrk.common.modules.order.dto.OrderBrandStatisticDto;
import com.rrk.common.modules.order.dto.ProvinceDto;
import com.rrk.order.manage.service.ITbOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单统计报表前端控制器
 *
 * @author dinghao
 * @date 2020-12-19
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/statistic")
public class OrderStatisticController {

    @Autowired
    private ITbOrderService orderService;

    /**
     * 根据时间和身份统计品牌订单量，销量和实付金额（柱状图）
     */
    @GetMapping(value = "/getOrderStatisticByBrand")
    @ApiOperation(value = "根据时间和身份统计品牌订单量，销量和实付金额（折现图）", httpMethod = "GET")
    public R<Object> getOrderStatisticByBrand(@RequestParam(value = "startTime") String startTime,
                                              @RequestParam(value = "endTime") String endTime,
                                              @RequestParam(value = "province", required = false) String province,
                                              @RequestParam(value = "category", required = false) String category
    ) {
        List<OrderBrandStatisticDto> list = orderService.getStatisticByBrand(startTime, endTime, province, category);
        return R.ok(200, "操作成功", list);
    }

//    /**
//     * 根据品牌统计订单数据(柱状图)
//     */
//    @ApiOperation(value = "根据品牌统计订单数据",httpMethod = "GET")
//    @GetMapping(value = "/getOrderStatisticByBrand")
//    public R<Object> getOrderStatisticByBrand(){
//      List<OrderBrandStatisticDto> list = orderService.getOrderStatisticByBrand();
//      return R.ok(200,"操作成功",list);
//
//    }

    /**
     * 查询每个商品分类下销量最好的商品信息(要是通过es则查询前10的商品)
     */
    @GetMapping(value = "/getOrderProductByOrder")
    @ApiOperation(value = "查询各省份（已付款，已发货，已完成）订单量", httpMethod = "GET")
    public R<Object> getOrderProductByOrder(@RequestParam(value = "startTime") String startTime,
                                            @RequestParam(value = "endTime") String endTime,
                                            @RequestParam(value = "category", required = false) String category) {

        List<OrderCategoryDto> list = orderService.getOrderProductByOrder(startTime, endTime, category);
        return R.ok(200, "操作成功", list);
    }

    /**
     * 不同商品spu在不同省份销量统计
     */
    @GetMapping(value = "/getOrderSpuStatistic")
    @ApiOperation(value = "不同商品spu在不同省份销量统计", httpMethod = "GET")
    public R<Object> getOrderSpuStatistic(@RequestParam(value = "startTime") String startTime,
                                          @RequestParam(value = "endTime") String endTime) {
        List<OrderProvinceDto> list = orderService.getOrderSpuStatistic(startTime, endTime);
        return R.ok(200, "操作成功", list);
    }

    /**
     * 订单用户统计分析（总人数，新人，老客户）
     */
    @GetMapping(value = "/getOrderUserStatistic")
    @ApiOperation(value = "订单用户统计分析（总人数，新人，老客户）（饼形图）")
    public R<Object> getOrderUserStatistic(@RequestParam(value = "startTime") String startTime,
                                           @RequestParam(value = "endTime") String endTime) {
        List<OrderUserStatisticDto> list = orderService.getOrderUserStatistic(startTime, endTime);
        return R.ok(200, "操作成功", list);

    }

    /**
     * 新用户按照时间统计首单购买的商品信息
     */
    @GetMapping(value = "/getOrderProductStatistic")
    @ApiOperation(value = "新用户按照时间统计首单购买的商品信息", httpMethod = "GET")
    public R<Object> getOrderProductStatistic(
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "startTime") String startTime,
            @RequestParam(value = "endTime") String endTime,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "category", required = false) String category) {

        IPage<OrderProductDto> page = orderService.getOrderProductStatistic(startTime, endTime, province, category, pageNo, pageSize);
        return R.ok(200, "操作成功", page);
    }

    /**
     * 各个省份下品牌销量前10的品牌数据
     */
    @GetMapping(value = "/getProvinceSku")
    @ApiOperation(value = "各个省份下品牌销量前3的品牌数据", httpMethod = "GET")
    public R<Object> getProvinceSku(@RequestParam(value = "startTime") String startTime,
                                    @RequestParam(value = "endTime") String endTime,
                                    @RequestParam(value = "category", required = false) String category) {

        List<ProvinceDto> list = orderService.getProvinceSku(startTime,endTime,category);
        //获取城市的经度和纬度
        if (CollUtil.isNotEmpty(list)) {
            for (ProvinceDto provinceDto : list) {

            }
        }

        return R.ok(200,"操作成功",list);
    }
}
