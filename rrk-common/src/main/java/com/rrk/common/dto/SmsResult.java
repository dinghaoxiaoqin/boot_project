package com.rrk.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 定义接收短信服务返回的数据
 */
@Data
public class SmsResult implements Serializable {

    private String Message;

    private String RequestId;

    private String Code;

    private String BizId;
}
