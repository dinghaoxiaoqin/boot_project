package com.rrk.common;

import com.rrk.common.handle.CustomExceptionEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 返回数据
 */
@ApiModel("API返回数据")
@Accessors(chain = true)
@Data
public class R<T> implements Serializable {

    private Integer code;
    private String msg;
    private  T data;
    public R(){}

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }



    /***
     * 过期
     *
     * @param msg:
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static R expired(String msg) {
        return new R(CustomExceptionEnum.UN_LOGIN.getCode(), msg, null);
    }

    public static R fail(String msg) {
        return new R(CustomExceptionEnum.FAILURE.getCode(), msg, null);
    }

    public static R noCode(String msg){
        return new R(CustomExceptionEnum.NO_CODE.getCode(),msg,null);

    }

    public static R loginFail(String msg){
        return new R(CustomExceptionEnum.LOGIN_FAIL.getCode(),msg,null);
    }

    /***
     * 自定义错误返回码
     *
     * @param code
     * @param message:
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static R fail(Integer code, String message) {
        return new R(code, message, null);
    }

    public static R ok(String message) {
        return new R(CustomExceptionEnum.SUCCESS.getCode(), message, null);
    }

    public static R ok() {
        return new R(CustomExceptionEnum.SUCCESS.getCode(), "OK", null);
    }

    public static R build(Integer code, String msg, Object data) {
        return new R(CustomExceptionEnum.SUCCESS.getCode(), msg, data);
    }

    public static R ok(String message, Object data) {
        return new R(CustomExceptionEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 自定义返回码
     */
    public static R ok(Integer code, String message) {
        return new R(code, message);
    }

    /**
     * 自定义
     *
     * @param code：验证码
     * @param message：返回消息内容
     * @param data：返回数据
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static R ok(Integer code, String message, Object data) {
        return new R(code, message, data);
    }


    public static R build(Integer code, String msg) {
        return new R(code, msg, null);
    }

    public R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R(T data) {
        this.code = CustomExceptionEnum.SUCCESS.getCode();
        this.msg = "OK";
        this.data = data;
    }

    public R(String message) {
        this(CustomExceptionEnum.SUCCESS.getCode(), message, null);
    }

    public R(String message, Integer code) {
        this.msg = message;
        this.code = code;
    }

    public R(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
