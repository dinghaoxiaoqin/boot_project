package com.rrk.common.modules.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限的dto
 */
@Data
public class MenuDto implements Serializable {

    /**
     * 权限id
     */
    @ApiModelProperty("权限id")
    private Integer menuId;
    /**
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    private String menuName;

    /**
     * 权限的路径
     */
    @ApiModelProperty("权限路径url")
    private String url;
}
