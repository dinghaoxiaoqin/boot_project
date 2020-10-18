package com.rrk.common.modules.manage.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态菜单列表
 */
@Data
public class PermissionDto implements Serializable {

    /**
     * 菜单id
     */
    private Long permissionId;

    /**
     * 父菜单id
     */
    private Long parentId;
    /**
     * 菜单名称
     */
    private String permissionName;

    /**
     * 菜单url
     */
    private String permissionUrl;

    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 前名称
     */
    private String name;

    /**
     * 菜单顺序
     */
    private Integer orderCount;
    /**
     *是否可用
     */
    private Integer hidden;

    /**
     * 子菜单
     */
    private List<PermissionDto> subMenuList = new ArrayList<>();


}
