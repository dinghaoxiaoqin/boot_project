package com.rrk.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.manage.dto.DelEmployeeDto;
import com.rrk.common.modules.manage.dto.EmployeeDto;
import com.rrk.common.modules.manage.dto.EmployeeInfo;
import com.rrk.common.modules.manage.dto.PermissionDto;
import com.rrk.common.modules.manage.entity.TbEmployee;
import com.rrk.common.modules.manage.entity.TbPart;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-04-25
 */
public interface ITbEmployeeService extends IService<TbEmployee> {

    IPage<TbEmployee> getEmployeeList(Integer pageNo, Integer pageSize, String keyword);

    /**
     * 根据管理员id获取动态菜单
     * @param employeeId
     * @return
     */
    List<PermissionDto> getPermissionsByUser(Long employeeId);

    /**
     * 获取用户信息
     * @param employeeId
     * @return
     */
    EmployeeInfo userInfo(Long employeeId);

    /**
     * 删除员工
     * @param delEmployeeDto
     * @return
     */
    Integer delEmployee(DelEmployeeDto delEmployeeDto);

    /**
     * 获取用户已经有的角色
     * @param employeeId
     * @return
     */
    List<TbPart> getRoleByAdmin(Long employeeId);

    /**
     * 后台用户账户密码登录
     * @param employeeDto
     * @return
     */
    String login(EmployeeDto employeeDto);
}
