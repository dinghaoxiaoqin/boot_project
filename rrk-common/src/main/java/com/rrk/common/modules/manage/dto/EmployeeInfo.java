package com.rrk.common.modules.manage.dto;

import com.rrk.common.modules.manage.entity.TbPart;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 返回用户管理数据
 */
@Data
public class EmployeeInfo implements Serializable {

    /**
     * DROP TABLE IF EXISTS `ums_menu`;
     * CREATE TABLE `ums_menu` (
     *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
     *   `parent_id` bigint(20) DEFAULT NULL COMMENT '父级ID',
     *   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     *   `title` varchar(100) DEFAULT NULL COMMENT '菜单名称',
     *   `level` int(4) DEFAULT NULL COMMENT '菜单级数',
     *   `sort` int(4) DEFAULT NULL COMMENT '菜单排序',
     *   `name` varchar(100) DEFAULT NULL COMMENT '前端名称',
     *   `icon` varchar(200) DEFAULT NULL COMMENT '前端图标',
     *   `hidden` int(1) DEFAULT NULL COMMENT '前端隐藏',
     *   PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COMMENT='后台菜单表';
     */
    /**
     * 用户id
     */
    private Long employeeId;
    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户头像
     */
    private String icon;
    /**
     * 用户角色信息
     */
    private List<TbPart> roles;

    /**
     * 用户权限信息
     */
    private List<PermissionDto> menus;
}
