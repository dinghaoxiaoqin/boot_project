package com.rrk.order.manage.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取跟踪物流的数据
 */
@Data
public class LogisticDto implements Serializable {

    private String name;

    private String time;
}
