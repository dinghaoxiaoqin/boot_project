package com.rrk.common.modules.user.dto.webdto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class UserAddressDto implements Serializable {


    private Long addressId;
    /**
     * userId
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户地址
     */
    private String address;
    /**
     * 用户手机号
     */
    private String userMobile;
    /**
     * 是否默认
     */
    private Integer isApprove;
}
