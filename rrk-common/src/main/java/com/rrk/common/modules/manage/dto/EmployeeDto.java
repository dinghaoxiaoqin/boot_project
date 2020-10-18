package com.rrk.common.modules.manage.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 添加员工
 */
@Data
@Setter
@Getter
public class EmployeeDto implements Serializable {

    /**
     * 员工编号
     */
    private Long employeeId;

    /**
     * 员工账号
     */
    private String username;

    /**
     * 员工密码
     */
    private String password;

    /**
     * 员工的图像
     */
    private String  icon;
    /**
     * 管理员手机号
     */
    private String phone;

    /**
     * 管理员地址
     */
    private String employeeAddress;

    /**
     * (0启用 1 禁用)
     */
    private Integer isUse;
}
