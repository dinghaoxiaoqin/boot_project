package com.rrk.product.controller;

import com.rrk.common.R;
import com.rrk.common.modules.product.dto.webdto.*;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.product.service.ITbCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 购物车表 前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@RestController
@RequestMapping("/cart")
@Slf4j
@CrossOrigin
public class TbCartController {

    @Autowired
    private ITbCartService cartService;

    /**
     * 商品添加购物车
     */
    @PostMapping(value = "/addOrder")
    public R<Object> addCart(HttpServletRequest request, @RequestBody List<AddCartDto> addCartDtos) {
        Long userId = JwtTokenUtil.getUserId(request);
        Boolean flag = cartService.addCart(userId, addCartDtos);
        return flag == true ? R.ok(200, "添加成功", "") : R.fail(400, "添加失败");
    }

    /**
     * 删除购物中的商品
     */
    @PostMapping(value = "/removeCart")
    public R<Object> removeCart(HttpServletRequest request, @RequestBody List<AddCartDto> addCartDtos) {
        Long userId = JwtTokenUtil.getUserId(request);
        Boolean flag = cartService.removeCart(userId, addCartDtos);
        return flag == true ? R.ok(200, "删除成功", "") : R.fail(400, "删除失败");

    }

    /**
     * 购物车列表
     */
    @GetMapping(value = "/getCarts")
    public R<Object> getCarts(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserId(request);
        List<CartDto> list = cartService.getCarts(userId);
        return R.ok(200, "操作成功", list);
    }

    /**
     * 获取结算列表
     */
    @GetMapping(value = "/getSettleList")
    public R<Object> getSettleList(HttpServletRequest request, @RequestBody SettleProVo settleProVo) {
        Long userId = JwtTokenUtil.getUserId(request);
        SettleDto settleDto = cartService.getSettleList(userId, settleProVo);
        return R.ok(200, "操作成功", settleDto);
    }

}

