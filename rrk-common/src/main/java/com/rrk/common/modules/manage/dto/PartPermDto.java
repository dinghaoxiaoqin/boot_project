package com.rrk.common.modules.manage.dto;

import com.rrk.common.modules.manage.entity.TbPermission;
import lombok.Data;

import java.io.Serializable;

@Data
public class PartPermDto implements Serializable {

    /**
     * 角色id
     */
    private Long partId;
    /**
     * 权限id
     */
    private Long permissionId;
    /**
     * 菜单父id
     */
    private Long parentId;
    /**
     * 权限url
     */
    private String permissionUrl;

    /**
     * 是否可用（0，可用，1 不可用）
     */
    private Integer isUse;

    public PartPermDto(Long partId, TbPermission permission) {
        this.partId = partId;
        this.permissionUrl = permission.getPermissionUrl();
        this.isUse = permission.getIsUse();
        this.permissionId = permission.getPermissionId();
        this.parentId = permission.getPermissionId();
    }

    public PartPermDto() {
    }
}
