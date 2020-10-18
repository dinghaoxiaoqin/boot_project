package com.rrk.common.modules.manage.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PartEmployeeDto implements Serializable {

    /**
     * 角色id
     */
    private Long partId;

    /**
     * 权限id
     */
    private List<Long> permissionIds;
}
