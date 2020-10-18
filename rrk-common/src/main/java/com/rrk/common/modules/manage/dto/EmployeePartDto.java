package com.rrk.common.modules.manage.dto;

import com.rrk.common.modules.manage.entity.TbPart;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePartDto implements Serializable {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */

    private Long partId;

    /**
     * 角色名称
     */
    private String partName;

    /**
     * 角色排序
     */
    private Integer partSort;

    /**
     * 是否可用（0可用，1不可用）
     */
    private Integer isUse;

    /**
     * 角色标记
     */
    private String partCode;

    public EmployeePartDto(TbPart part, Long userId) {
        this.isUse = part.getIsUse();
        this.partCode = part.getPartCode();
        this.partId = part.getPartId();
        this.partName = part.getPartName();
        this.partCode = part.getPartCode();
        this.partSort = part.getPartSort();
        this.userId = userId;
    }

    public EmployeePartDto(){

    }


}
