package com.rrk.common.modules.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 将登录用户的基本信息和token保存到redis实体
 */
@Data
public class UserRedisDto implements Serializable {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户的昵称
     */
    private String nickName;
    /**
     * 用户的名称
     */
    private String username;
    /**
     * 用户的token
     */
    private String token;

}
