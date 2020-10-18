package com.rrk.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 手机号验证码登录实体
 */
@Data
public class SmsLoginDto implements Serializable {

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 手机验证码
     */
    private String smsCode;
    /**
     * 0 后台用户 1移动端用户
     */
    private Integer deviceType;
}
