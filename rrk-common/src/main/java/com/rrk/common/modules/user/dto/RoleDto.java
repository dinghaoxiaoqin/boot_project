package com.rrk.common.modules.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息
 */
@Data
@ApiModel("用户角色信息实体")
public class RoleDto implements Serializable {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private Integer roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    private String roleDesc;
    /**
     * 用户的权限名称
     */
    @ApiModelProperty("用户的权限名称")
    private List<MenuDto> menuDtoList;
}
