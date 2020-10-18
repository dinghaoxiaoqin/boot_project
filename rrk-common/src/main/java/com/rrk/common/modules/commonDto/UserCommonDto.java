package com.rrk.common.modules.commonDto;

import cn.hutool.core.collection.CollUtil;
import com.rrk.common.modules.manage.entity.TbEmployee;
import com.rrk.common.modules.manage.entity.TbPart;
import com.rrk.common.modules.manage.entity.TbPermission;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dinghao
 * app,pc,后台用户信息的公共实体
 */
@Data
public class UserCommonDto implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户账户
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户微博号
     */
    private String weiPo;
    /**
     * 用户微信
     */
    private String weiXin;
    /**
     * 用户qq
     */
    private String qq;
    /**
     * 用户角色
     */
    private List<RoleCommonDto> roleCommonDtos;
    /**
     * 用户权限
     */
    private List<PermissionCommonDto> permissionCommonDtos;

    /**
     * 用户来源（0 后台管理 ，1 前台）
     *
     */
    private Integer userResource ;
    public UserCommonDto() {
    }

    public UserCommonDto(List<TbPart> partList, List<TbPermission> tbPermissions, TbEmployee employee) {
        this.userId = employee.getEmployeeId();
        this.username = employee.getUsername();
        this.password = employee.getPassword();
        this.phone = employee.getPhone();
        this.userResource = 0;
        List<RoleCommonDto> roleCommonDtoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(partList)) {
            for (TbPart part : partList) {
                RoleCommonDto roleCommonDto = new RoleCommonDto(part);
                roleCommonDtoList.add(roleCommonDto);
            }
        }
        List<PermissionCommonDto> permissionCommonDtos = new ArrayList<>();
        if (CollUtil.isNotEmpty(tbPermissions)) {
            for (TbPermission tbPermission : tbPermissions) {
                PermissionCommonDto permissionCommonDto = new  PermissionCommonDto(tbPermission);
                permissionCommonDtos.add(permissionCommonDto);
            }
        }
        this.roleCommonDtos = roleCommonDtoList;
        this.permissionCommonDtos = permissionCommonDtos;
    }
}
