package com.rrk.common.handle;

import java.io.Serializable;

/**
 *
 */
public class MyException extends RuntimeException implements Serializable {
    //异常处理类
    public MyException(){
        super();
    }

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private Throwable throwable;

    public MyException(int code) {
        this.code = code;
    }

    public MyException(String message) {
        this.code = 500;
        this.msg=message;
    }
    public MyException(String message, Throwable e){
           this.code = 500;
           this.msg = message;
           this.throwable = e;
    }

    public MyException(int code, String mgs) {
        this.code = code;
        this.msg = mgs;
    }

    public MyException(int code, String message, Throwable e) {
        this.code = code;
        this.msg = message;
        this.throwable = e;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
