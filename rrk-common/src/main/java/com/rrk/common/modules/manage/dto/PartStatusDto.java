package com.rrk.common.modules.manage.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PartStatusDto implements Serializable {

    private Long partId;

    private Integer isUse;
}
