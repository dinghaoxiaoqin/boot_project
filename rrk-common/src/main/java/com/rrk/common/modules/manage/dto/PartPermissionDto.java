package com.rrk.common.modules.manage.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色分配菜单实体
 */
@Data
public class PartPermissionDto implements Serializable {

    /**
     * 菜单id
     */
    private Long permissionId;

    private String permissionName;

    private List<PartPermissionDto> children;
}
