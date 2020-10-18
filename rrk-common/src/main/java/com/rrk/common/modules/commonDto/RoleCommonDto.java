package com.rrk.common.modules.commonDto;

import com.rrk.common.modules.manage.entity.TbPart;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色
 */
@Data
public class RoleCommonDto implements Serializable {
    /**
     * 用户角色Id
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;
    public RoleCommonDto() {
    }

    public RoleCommonDto(TbPart part) {
        this.roleId = part.getPartId();
        this.roleName = part.getPartName();
    }
}
