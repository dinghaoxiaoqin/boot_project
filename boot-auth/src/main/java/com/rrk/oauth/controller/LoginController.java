package com.rrk.oauth.controller;

import com.rrk.common.R;
import com.rrk.common.dto.SmsLoginDto;
import com.rrk.common.modules.manage.dto.EmployeeDto;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.oauth.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户登录的前端控制器
 *
 * @author dinghao
 * @date 2020-6-30
 */

@RestController
@RequestMapping("/oauth")
@CrossOrigin
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 后台用户登录(账号密码登录)
     */
    @PostMapping(value = "/login")
    public R<Object> login(@RequestBody EmployeeDto employeeDto) {
        String token = loginService.login(employeeDto);
        return R.ok(200, "登录成功", token);
    }

    /**
     * 登出操作
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/logout")
    public R<Object> logout(HttpServletRequest request) {
        Long userId = JwtTokenUtil.getUserId(request);
        if (userId != null) {
            Integer count = loginService.logout(request);
            if (count > 0) {
                return R.ok(200, "登出成功");
            } else {
                return R.fail(437, "登出失败");
            }
        } else {
            return R.fail(401, "登出用户不存在，请联系管理员");
        }
    }

    /**
     * 后台管理系统（手机号验证码登录）
     */
    @PostMapping(value = "/smsLogin")
    public R<Object> smsLogin(@RequestBody SmsLoginDto smsLoginDto) {
       // smsLoginDto.setDeviceType(NumConstants.MANAGE_USER);
        String token = loginService.smsLogin(smsLoginDto);
        return R.ok(200, "登录成功", token);
    }



}
