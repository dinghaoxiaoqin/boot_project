package com.rrk.common.modules.manage.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserPartDto implements Serializable {

    /**
     * 用户id
     */
    private Long adminId;

    /**
     * 用户角色
     */
    private List<Long> roleIds;
}
