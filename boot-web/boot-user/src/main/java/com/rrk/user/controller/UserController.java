package com.rrk.user.controller;

import com.rrk.common.R;
import com.rrk.common.modules.user.dto.webdto.UserDto;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.user.service.ITbUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户前端控制器
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private ITbUserService userService;

    /**
     * 根据用户id获取用户信息
     */

    @GetMapping(value = "/getUserById")
    public R<Object> getUserById(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        TbUser user = userService.getUserById(userId);
        return R.ok(200, "操作成功", user);

    }

    /**
     * 编辑用户信息
     */
    @PostMapping(value = "/editUser")
    public R<Object> editUser(HttpServletRequest request, @RequestBody UserDto userDto) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId == null) {
            return R.fail(401, "请先登录");
        }
        userDto.setUserId(userId);
        Integer count = userService.editUser(userDto);
        if (count > 0) {
            return R.ok(200,"编辑成功");
        } else {
            return R.fail(401,"修改用户信息失败");
        }

    }

}
