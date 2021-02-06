package com.rrk.common.modules.user.dto;

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

    private Integer menuId;
    /**
     * 权限名称
     */

    private String menuName;

    /**
     * 权限的路径
     */

    private String url;
}
