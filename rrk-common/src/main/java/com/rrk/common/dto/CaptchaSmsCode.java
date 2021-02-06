package com.rrk.common.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 短信验证的实体
 */
@Data

public class CaptchaSmsCode implements Serializable {

    /**
     * 短信验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 手机号
     */
    private String mobile;

    public CaptchaSmsCode(){}

    public CaptchaSmsCode(String code,int expireAfterSecodes,String mobile){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSecodes);
        this.mobile = mobile;
    }

    /**
     * 是否过期
     */
    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode(){
        return code;
    }

    public String getMobile(){
        return mobile;
    }
}
