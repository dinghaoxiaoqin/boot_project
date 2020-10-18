package com.rrk.common.modules.commonDto;

import com.rrk.common.modules.manage.entity.TbPermission;
import lombok.Data;

import java.io.Serializable;

/**
 * 公共用户权限信息
 */
@Data
public class PermissionCommonDto implements Serializable {

    /**
     * 权限id
     */
    private Long permissionId;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限url
     */
    private String permissionUrl;
    public PermissionCommonDto() {
    }

    public PermissionCommonDto(TbPermission tbPermission) {
        this.permissionId = tbPermission.getPermissionId();
        this.permissionName = tbPermission.getPermissionName();
        this.permissionUrl = tbPermission.getPermissionUrl();
    }
}
