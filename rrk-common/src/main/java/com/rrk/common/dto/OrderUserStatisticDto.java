package com.rrk.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderUserStatisticDto implements Serializable {

    private String name;

    //总人数
    private Integer userCount;

}
