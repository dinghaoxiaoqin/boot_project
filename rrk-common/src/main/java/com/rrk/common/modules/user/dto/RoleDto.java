package com.rrk.common.modules.user.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息
 */
@Data

public class RoleDto implements Serializable {

    /**
     * 角色id
     */

    private Integer roleId;

    /**
     * 角色名称
     */

    private String roleName;

    /**
     * 角色描述
     */

    private String roleDesc;
    /**
     * 用户的权限名称
     */

    private List<MenuDto> menuDtoList;
}
