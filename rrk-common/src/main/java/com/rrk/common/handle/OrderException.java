package com.rrk.common.handle;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderException extends RuntimeException implements Serializable {

    public OrderException(){
        super();
    }
    private int code;

    private String msg;


    public OrderException(int code, String mgs) {
        this.code = code;
        this.msg = mgs;
    }
}
