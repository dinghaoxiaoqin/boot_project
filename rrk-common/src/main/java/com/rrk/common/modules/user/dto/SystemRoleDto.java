package com.rrk.common.modules.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统角色管理信息
 */
@ApiModel("系统角色管理信息")
@Data
public class SystemRoleDto implements Serializable {

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private Integer roleId;
    /**
     * 角色名称(汉字)
     */
    @ApiModelProperty("角色名称(汉字)")
    private String roleName;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    private String roleDesc;

    /**
     * 角色的英文code.如：ADMIN
     */
    @ApiModelProperty("角色的英文code.如：ADMIN")
    private String roleCode;

    /**
     * 角色顺序
     */
    @ApiModelProperty("角色顺序")
    private Integer sort;

    /**
     * 0表示可用
     */
    @ApiModelProperty("0表示可用,1表示不可用")
    private Integer status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
