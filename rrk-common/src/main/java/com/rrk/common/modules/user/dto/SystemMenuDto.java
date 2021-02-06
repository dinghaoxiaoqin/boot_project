package com.rrk.common.modules.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 添加系统菜单信息
 */
@Data

public class SystemMenuDto implements Serializable {

    /**
     * 菜单id
     */

    private Integer menuId;

    /**
     * 父菜单ID
     */

    private Integer menuPid;

    /**
     * 当前菜单所有父菜单
     */

    private String menuPids;

    /**
     * 0:不是叶子节点，1:是叶子节点
     */

    private Integer isLeaf;

    /**
     * 菜单名称
     */

    private String menuName;

    /**
     * 跳转URL
     */

    private String url;
    /**
     * 菜单图标
     */

    private String icon;
    /**
     * 排序
     */

    private Integer sort;

    /**
     * 菜单层级
     */

    private Integer level;

    /**
     * 0:启用,1:禁用
     */

    private Integer status;

    /**
     * 创建时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 菜单唯一标识
     */

    private String menuSign;

}
