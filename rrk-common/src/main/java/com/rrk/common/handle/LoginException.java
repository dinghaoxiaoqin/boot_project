package com.rrk.common.handle;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录异常
 */
@Data
public class LoginException extends RuntimeException implements Serializable {


    public LoginException(){
        super();
    }
    private int code;

    private String msg;


    public LoginException(int code, String mgs) {
        this.code = code;
        this.msg = mgs;
    }
}
