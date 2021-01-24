package com.rrk.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatisticDto implements Serializable {

        private String skuName;

        private Integer numsMax;

}
