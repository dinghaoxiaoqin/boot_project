package com.rrk.manage.service.impl;

import com.rrk.common.modules.manage.entity.TbEmployeePart;
import com.rrk.common.modules.manage.entity.TbPart;
import com.rrk.common.modules.manage.entity.TbPartPermission;
import com.rrk.common.modules.manage.entity.TbPermission;
import com.rrk.manage.service.*;
import com.rrk.manage.utils.TokenRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 初始数据服务层
 *
 * @author dinghao
 */
@Service
@Slf4j
public class InitServiceImpl implements InitService {

    @Autowired
    private ITbPermissionService permissionService;

    @Autowired
    private ITbPartService partService;

    @Autowired
    private ITbEmployeePartService employeeService;
    @Autowired
    private ITbPartPermissionService partPermissionService;

    @Autowired
    private TokenRedisUtil tokenRedisUtil;

    @Override
    public void initPartAndPermission() {
        List<TbPermission> list = permissionService.list(null);
        List<TbEmployeePart> employeePartList = employeeService.list(null);
        List<TbPartPermission> partPermissions = partPermissionService.list(null);
        List<TbPart> tbParts = partService.list(null);
        tokenRedisUtil.initPermission(list, partPermissions);
        tokenRedisUtil.initPart(tbParts, employeePartList);

    }
}
