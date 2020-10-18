package com.rrk.oauth.service;

import com.rrk.common.dto.SmsLoginDto;
import com.rrk.common.modules.manage.dto.EmployeeDto;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    /**
     * 后台用户登录
     * @param employeeDto
     * @return
     */
    String login(EmployeeDto employeeDto);

    /**
     * 用户登出
     * @param request
     * @return
     */
    Integer logout(HttpServletRequest  request);

    /**
     * 手机验证码登录
     * @param smsLoginDto
     * @return
     */
    String smsLogin(SmsLoginDto smsLoginDto);

    /**
     * 移动端手机短信登录
     * @param smsLoginDto
     * @return
     */
    String webSmsLogin(SmsLoginDto smsLoginDto);

    /**
     * 第三方登录（社交登录）
     * @param code
     * @return
     */
    String appLogin(String code,Integer id);
}
