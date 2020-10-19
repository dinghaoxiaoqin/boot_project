package com.rrk.user.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.R;
import com.rrk.common.modules.user.dto.webdto.AddressDto;
import com.rrk.common.modules.user.dto.webdto.AddressInfo;
import com.rrk.common.modules.user.dto.webdto.UdateAddressDto;
import com.rrk.common.modules.user.dto.webdto.UserAddressDto;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.user.service.ITbUserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-09-01
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class TbUserAddressController {

    @Autowired
    private ITbUserAddressService userAddressService;

    /**
     * 获取用户的默认选中的地址
     */
    @GetMapping(value = "/getDefaultAddress")
    public R<Object> getDefaultAddress(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        UserAddressDto userAddressDto = userAddressService.getDefaultAddress(userId);
        return R.ok(200, "操作成功", userAddressDto);
    }

    /**
     * 获取用户地址列表
     */
    @GetMapping(value = "/getAddressList")
    public R<Object> getAddressList(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        List<AddressDto> list = userAddressService.getAddressList(userId);
        return R.ok(200, "操作成功", list);
    }

    /**
     * 编辑用户地址
     */
    @PostMapping(value = "/addAddress")
    public R<Object> addAddress(HttpServletRequest request, @RequestBody AddressInfo addressInfo) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        int num = userAddressService.count(new QueryWrapper<TbUserAddress>().eq("user_id", userId));
        if (num > 6) {
            return R.fail(436, "最多只能添加6条地址");
        }
        Integer count = userAddressService.addAddress(userId, addressInfo);
        if (count > 0) {
            return R.ok(200, "操作成功", "");
        }
        return R.ok(400, "失败");
    }

    /**
     * 获取要修改的用户地址
     */
    @GetMapping(value = "/getUpdateAddress")
    public R<Object> getUpdateAddress(HttpServletRequest request, @RequestParam(value = "id") String id) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        TbUserAddress one = userAddressService.getOne(new QueryWrapper<TbUserAddress>().eq("id", Convert.toLong(id)));
        UdateAddressDto dto = new UdateAddressDto(one);
        return R.ok(200, "操作成功", dto);
    }

    /**
     * 选中收货地址
     */
    @PostMapping(value = "/selectAddress")
    public R<Object> selectAddress(HttpServletRequest request, @RequestParam(value = "id") Long id){
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        Integer count = userAddressService.selectAddress(userId,id);
        return  R.ok(200,"操作成功");
    }
    /**
     * 删除地址
     */
}

