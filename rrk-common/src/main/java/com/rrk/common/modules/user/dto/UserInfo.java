package com.rrk.common.modules.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 返回用户的基本信息
 */
@Data
@Getter
@Setter
public class UserInfo implements Serializable {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;
    /**
     * 用户账户
     */
    @ApiModelProperty("用户账户")
    private String username;
    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickName;
    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String userSrc;
    /**
     * 用户是否可用（0无效用户，1有效用户）
     */
    @ApiModelProperty("用户是否可用（0无效用户，1有效用户）")
    private Integer enabled;

    /**
     * 用户手机号
     */
    @ApiModelProperty("用户手机号")
    private String phone;
    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;
    /**
     * 用户注册时间
     */
    @ApiModelProperty("用户注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 用户的角色名称
     */
    private List<RoleDto> roleDtoList;


}
