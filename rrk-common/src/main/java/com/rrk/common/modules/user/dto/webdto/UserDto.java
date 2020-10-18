package com.rrk.common.modules.user.dto.webdto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 */
@Data
public class UserDto implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String userSrc;

    /**
     * 用户等级
     */
    private Integer userRank;

}
