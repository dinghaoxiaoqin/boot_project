package com.rrk.order.utils;

/**
 * @description: 返回枚举类
 * @author: Administrator
 * @date: 2019-08-10 16:51
 */
public enum RetEnum {


    // 0000: 成功
    RET_SUCCESS("0000", ""),


    // 失败(01开始标示参数校验相关错误码)
    RET_PARAM_NOT_FOUND("0101", "参数不存在"),
    RET_PARAM_INVALID("0102", "无效的参数"),

    //  失败(02开始标示DB操作相关错误码)
    RET_DB_FAIL("0201", "数据库操作失败"),

    // 业务相关
    RET_BIZ_DATA_NOT_EXISTS("1001", "数据不存在"),
    RET_CHANNEL_NOT_EXISTS("1002", "商户不存在此支付渠道"),
    RET_NOT_SUPPOT_PAYMENT("1003", "商家不支持此支付方式"),


    // 未知错误
    RET_PAY_ERROR("9999", "支付下单失败"),
    RET_ORDER_QUERY_ERROR("9999", "下单账单查询失败"),
    RET_UNKNOWN_ERROR("9999", "未知错误");

    private String code;
    private String message;

    private RetEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static RetEnum getRetEnum(String code) {
        if (code == null) {
            return null;
        }

        RetEnum[] values = RetEnum.values();
        for (RetEnum e : values) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
