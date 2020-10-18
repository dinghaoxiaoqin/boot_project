package com.rrk.common.modules.manage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工列表
 */
@Data
@Setter
@Getter
public class EmployeeVo implements Serializable {

    /**
     * 员工编号
     */
    private Long employeeId;
    /**
     * 员工名称
     */
    private String username;

    /**
     * 管理员手机号
     */
    private String phone;

    /**
     * 管理员地址
     */
    private String employeeAddress;

    /**
     * 员工的角色id
     */
    private Long partId;
    /**
     * 员工的角色名称
     */
    private String partName;

    /**
     * 角色的标记
     */
    private String partCode;

    /**
     * 员工的权限id
     */
    private Long permissionId;
    /**
     * 员工的权限名称
     */
    private String permissionName;
    /**
     * 员工的权限url
     */
    private String permissionUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date createTime;



}
