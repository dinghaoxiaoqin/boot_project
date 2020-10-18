package com.rrk.oauth.controller;

import com.rrk.common.R;
import com.rrk.common.dto.SmsLoginDto;
import com.rrk.common.modules.commonConstant.CommonContants;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.oauth.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 移动端登录
 */
@RestController
@RequestMapping("/oauth")
@CrossOrigin
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AppLoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 移动端手机登录
     */
    @PostMapping(value = "/webSmsLogin")
    public R<Object> webSmsLogin(@RequestBody SmsLoginDto smsLoginDto) {
        String token = loginService.webSmsLogin(smsLoginDto);
        return R.ok(200, "登录成功", token);
    }

    /**
     * 第三方授权登录（微博授权登录（授权码登录））
     */
    @GetMapping(value = "/appLogin")
    public R<Object> appLogin(String code){
        String token = loginService.appLogin(code, CommonContants.WEIBO_ID);
        return R.ok(200, "登录成功", token);
    }
    /**
     * 第三方微信登录
     */
    @GetMapping(value = "/weiXinLogin")
    public R<Object> weiXinLogin(String code){
        String token = loginService.appLogin(code, CommonContants.WEICHAT_ID);
        return R.ok(200, "登录成功", token);
    }

    /**
     * 用户退出登录
     */
    @PostMapping(value = "/appLoginout")
    public R<Object> appLoginout(HttpServletRequest request){
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

}
