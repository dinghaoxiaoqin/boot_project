package com.rrk.common.modules.manage.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParentMenuDto implements Serializable {

    /**
     * 菜单id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String parentName;
}
