package com.rrk.common.handle;

public enum CustomExceptionEnum  {




    ADD_USER_ERROR(501, "添加用户失败"),
    GET_USER_ERROR(502,"获取用户异常"),

    //成功
    SUCCESS( 200, "SUCCESS" ),
    //失败
    FAILURE( 400, "FAILURE" ),
    // 未登录
    UN_LOGIN( 401, "未登录" ),
    //未认证（签名错误、token错误）
    UNAUTHORIZED( 403, "未认证或Token失效" ),
    //未通过认证
    USER_UNAUTHORIZED( 402, "用户名或密码不正确" ),
    //接口不存在
    NOT_FOUND( 404, "接口不存在" ),
    //服务器内部错误
    INTERNAL_SERVER_ERROR( 500, "未知异常，请联系管理员" ),
    //请联系管理员
    GET_USER_EXCEPTION(505,"token不存在，请联系管理员"),

     //登录认证失败
    FAIL_AUTHENTICATION(418,"登录认证失败"),

    NO_CODE(417,"验证码不存在"),

    FAIL_CODE(419,"验证码输入错误"),
    LOGIN_FAIL(420,"请先登录");

    public int code;
    public String message;
    private CustomExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    CustomExceptionEnum(String message) {
        this.code = 500;
        this.message = message;
    }



    public static String getMessage(int code) {
        for (CustomExceptionEnum c : CustomExceptionEnum.values()) {
            if (c.getCode() == code) {
                return c.message;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
