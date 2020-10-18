package com.rrk.common.modules.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.File;
import java.io.Serializable;

@Data
@ApiModel(value = "用户基本信息的实体")
public class UserDto implements Serializable {


    /**
     * 用户图像
     */
    @ApiModelProperty(value = "用户图像")
    private File userSrc;
    /**
     * 用户详细地址
     */
    @ApiModelProperty(value = "用户地址")
    private String address;
    /**
     * 用户账户
     */
    @ApiModelProperty(value = "用户账户")
    private String userName;
    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;
    /**
     * 用户手机号
     */
    @ApiModelProperty("用户手机号")
    private String mobile;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String password;
    /**
     * 短信验证码
     */
    @ApiModelProperty("短信验证码")
    private String smsCode;

}
