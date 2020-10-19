package com.rrk.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.commonDto.UserCommonDto;
import com.rrk.common.modules.manage.dao.TbEmployeeMapper;
import com.rrk.common.modules.manage.dao.TbEmployeePartMapper;
import com.rrk.common.modules.manage.dto.DelEmployeeDto;
import com.rrk.common.modules.manage.dto.EmployeeDto;
import com.rrk.common.modules.manage.dto.EmployeeInfo;
import com.rrk.common.modules.manage.dto.PermissionDto;
import com.rrk.common.modules.manage.entity.*;
import com.rrk.common.utils.JwtUtil;
import com.rrk.common.utils.Md5Util;
import com.rrk.manage.service.*;
import com.rrk.manage.utils.TokenRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-04-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbEmployeeServiceImpl extends ServiceImpl<TbEmployeeMapper, TbEmployee> implements ITbEmployeeService {

    @Autowired
    private TbEmployeeMapper employeeMapper;

    @Autowired
    private ITbEmployeeService employeeService;
    @Autowired
    private TbEmployeePartMapper employeePartMapper;
    @Autowired
    private ITbPartService partService;
    @Autowired
    private ITbEmployeePartService employeePartService;
    @Autowired
    private ITbPartPermissionService partPermissionService;
    @Autowired
    private ITbPermissionService permissionService;

    @Autowired
    private TokenRedisUtil tokenRedisUtil;

    /**
     * 获取系统用户数据
     *
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    @Override
    public IPage<TbEmployee> getEmployeeList(Integer pageNo, Integer pageSize, String keyword) {
        log.info("接收传入的参数：pageNo->{},pageSize->{},keyword->{}", pageNo, pageSize, keyword);
        Page<TbEmployee> page = new Page<>(pageNo, pageSize);
        if (StrUtil.isNotBlank(keyword)) {
            IPage<TbEmployee> employeeIPage = employeeService.page(page, new QueryWrapper<TbEmployee>().like("username", keyword));
            return employeeIPage;
        } else {
            IPage<TbEmployee> employeeIPage = employeeService.page(page);
            return employeeIPage;
        }
    }

    /**
     * 获取动态菜单
     *
     * @param employeeId
     * @return
     */
    @Override
    public List<PermissionDto> getPermissionsByUser(Long employeeId) {
        List<PermissionDto> dtoList = new ArrayList<>();
        List<TbPermission> list = employeeMapper.getPermissionsByUser(employeeId);
        if (CollUtil.isNotEmpty(list)) {
            for (TbPermission tbPermission : list) {
                PermissionDto permissionDto = new PermissionDto();
                permissionDto.setIcon(tbPermission.getIcon());
                permissionDto.setOrderCount(tbPermission.getOrderCount());
                permissionDto.setParentId(tbPermission.getParentId());
                permissionDto.setPermissionName(tbPermission.getPermissionName());
                permissionDto.setPermissionUrl(tbPermission.getPermissionUrl());
                permissionDto.setPermissionId(tbPermission.getPermissionId());
                permissionDto.setName(tbPermission.getName());
                permissionDto.setHidden(tbPermission.getIsUse());
                dtoList.add(permissionDto);
            }
        }
        return dtoList;
    }

    /**
     * 获取用户信息
     *
     * @param employeeId
     * @return
     */
    @Override
    public EmployeeInfo userInfo(Long employeeId) {
        EmployeeInfo info = new EmployeeInfo();
        //1用户信息
        TbEmployee employee = employeeService.getOne(new QueryWrapper<TbEmployee>().eq("employee_id", employeeId));
        //2角色信息
        List<TbPart> parts = employeePartMapper.getPartByEmployeeId(employee.getEmployeeId());
        //3权限信息
        List<PermissionDto> permissionsByUser = getPermissionsByUser(employeeId);
        info.setEmployeeId(employeeId);
        info.setUsername(employee.getUsername());
        info.setIcon(employee.getIcon());
        info.setMenus(permissionsByUser);
        info.setRoles(parts);
        return info;
    }

    /**
     * 删除员工
     *
     * @param delEmployeeDto
     * @return
     */
    @Override
    public Integer delEmployee(DelEmployeeDto delEmployeeDto) {
        boolean flag = employeeService.remove(new QueryWrapper<TbEmployee>().eq("employee_id", delEmployeeDto.getEmployeeId()));
        List<TbEmployeePart> tbEmployeeParts = employeePartService.list(new QueryWrapper<TbEmployeePart>().eq("employee_id", delEmployeeDto.getEmployeeId()));
        if (CollUtil.isNotEmpty(tbEmployeeParts)) {
            boolean remove = employeePartService.remove(new QueryWrapper<TbEmployeePart>().eq("employee_id", delEmployeeDto.getEmployeeId()));
            return flag && remove ? 1 : 0;
        } else {
            return flag ? 1 : 0;
        }
    }

    /**
     * 获取用户已有角色
     *
     * @param employeeId
     * @return
     */
    @Override
    public List<TbPart> getRoleByAdmin(Long employeeId) {
        List<TbPart> partList = new ArrayList<>();
        List<TbEmployeePart> list = employeePartService.list(new QueryWrapper<TbEmployeePart>().eq("employee_id", employeeId));
        if (CollUtil.isNotEmpty(list)) {
            partList = partService.list(new QueryWrapper<TbPart>().in("part_id", list.stream().map(l -> l.getPartId()).collect(Collectors.toList())));
        }
        return partList;
    }

    /**
     * 后台用户账户密码登录
     *
     * @param employeeDto
     * @return
     */
    @Override
    public String login(EmployeeDto employeeDto) {
        //1，校验账户是否存在
        TbEmployee employee = employeeService.getOne(new QueryWrapper<TbEmployee>().eq("username", employeeDto.getUsername()));
        if (employee == null) {
            throw new MyException("账户不存在，请联系管理员");
        }
        if (StrUtil.equals("admin", employeeDto.getUsername())) {
            if (!StrUtil.equals(employeeDto.getPassword(), employee.getPassword())) {
                throw new MyException("密码不正确，请重新输入！");
            }
        } else {
            //2校验密码
             String password = Md5Util.convertMD5(Md5Util.convertMD5(employee.getPassword()));
            if (!StrUtil.equals(employeeDto.getPassword(),password)) {
                throw new MyException("密码不正确，请重新输入！");
            }
        }
        List<TbPart> partList = new ArrayList<>();
        List<TbPermission> tbPermissions = new ArrayList<>();
        //3.将用户的角色和权限都保存到jwt里面
        List<TbEmployeePart> employeePartList = employeePartService.list(new QueryWrapper<TbEmployeePart>().eq("employee_id", employee.getEmployeeId()));
        if (ObjectUtil.isNotNull(employeePartList)) {
            List<Long> collect = employeePartList.stream().map(u -> u.getPartId()).collect(Collectors.toList());
            partList = partService.list(new QueryWrapper<TbPart>().in("part_id", collect));
            List<TbPartPermission> tbPartPermissions = partPermissionService.list(new QueryWrapper<TbPartPermission>().in("part_id", collect));
            if (CollUtil.isNotEmpty(tbPartPermissions)) {
                tbPermissions = permissionService.list(new QueryWrapper<TbPermission>().in("permission_id", tbPartPermissions.stream().map(p -> p.getPermissionId()).collect(Collectors.toList())));
            }
        }
        UserCommonDto userCommonDto = new UserCommonDto(partList,tbPermissions,employee);
        String token = JwtUtil.generateToken(userCommonDto);
        // tokenRedisUtil.addUserToRedis(employeeSecurity, token);
        return token;
    }
}