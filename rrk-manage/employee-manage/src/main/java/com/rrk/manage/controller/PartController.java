package com.rrk.manage.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.manage.dto.PartEmployeeDto;
import com.rrk.common.modules.manage.dto.PartStatusDto;
import com.rrk.common.modules.manage.dto.UserPartDto;
import com.rrk.common.modules.manage.entity.TbEmployeePart;
import com.rrk.common.modules.manage.entity.TbPart;
import com.rrk.common.modules.manage.entity.TbPartPermission;
import com.rrk.common.modules.manage.entity.TbPermission;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.manage.service.ITbEmployeePartService;
import com.rrk.manage.service.ITbPartPermissionService;
import com.rrk.manage.service.ITbPartService;
import com.rrk.manage.service.ITbPermissionService;
import com.rrk.manage.utils.TokenRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/manage")
@CrossOrigin
@Slf4j
public class PartController {
    @Autowired
    private ITbPartService partService;

    @Autowired
    private ITbPartPermissionService partPermissionService;

    @Autowired
    private ITbPermissionService permissionService;

    @Autowired
    private TokenRedisUtil tokenRedisUtil;

    @Autowired
    private ITbEmployeePartService employeePartService;

    /**
     * 添加角色（只有超级管理员才有该权限）
     */
    @PostMapping(value = "/addPart")
    public R<Object> addPart(HttpServletRequest request, @RequestBody TbPart part) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            //判断该角色是否存在
            TbPart one = partService.getOne(new QueryWrapper<TbPart>().eq("part_id", part.getPartId()));
            if (ObjectUtil.isNotNull(one)) {
                boolean update = partService.update(part, new QueryWrapper<TbPart>().eq("part_id", part.getPartId()));
                if (update) {
                    // tokenRedisUtil.editRole(part,employeeId);
                    return R.ok(200, "修改成功", "");
                }
                return R.fail(411, "修改角色失败");
            }
            part.setCreateTime(new Date());
            part.setIsUse(0);
            part.setPartSort(1);
            boolean save = partService.save(part);
            //tokenRedisUtil.editRole(part,employeeId);
            if (save) {
                return R.ok(200, "添加角色成功");
            } else {
                return R.fail(408, "添加角色失败");
            }
        } else {
            return R.fail(401, "请先登录");
        }

    }

    /**
     * 获取角色
     */
    @GetMapping(value = "/getParts")
    public R<Object> getParts(HttpServletRequest request) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            List<TbPart> list = partService.list();
            if (CollUtil.isNotEmpty(list)) {
                return R.ok(200, "操作成功", list);
            } else {
                return R.fail(408, "获取角色列表失败");
            }
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * 角色列表
     *
     * @param request
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    @GetMapping(value = "/getPartList")
    public R<Object> getPartList(HttpServletRequest request,
                                 @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "keyword", required = false) String keyword) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            IPage<TbPart> page = partService.getPartList(pageNo, pageSize, keyword);
            return R.ok(200, "操作成功", page);
        } else {
            return R.fail(401, "请先登录");
        }

    }

    /**
     * 根据角色id获取菜单信息
     */
    @GetMapping(value = "/getPartMenu")
    public R<Object> getPartMenu(HttpServletRequest request,
                                 @RequestParam(value = "partId") Long partId) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        List<TbPermission> tbPermissions = new ArrayList<>();
        if (employeeId != null) {
            List<TbPartPermission> list = partPermissionService.
                    list(new QueryWrapper<TbPartPermission>().eq("part_id", partId));
            if (CollUtil.isNotEmpty(list)) {
                tbPermissions = permissionService.list(new QueryWrapper<TbPermission>().in("permission_id", list.stream().map(l -> l.getPermissionId()).collect(Collectors.toList())));
            }
            return R.ok(200, "操作成功", tbPermissions);
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * 给角色添加权限
     */
    @PostMapping(value = "/addPartPermission")
    public R<Object> addPartPermission(HttpServletRequest request, @RequestBody PartEmployeeDto partEmployeeDto) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            List<TbPartPermission> list = new ArrayList<>();
            //判断分配权限是否重复
            List<TbPermission> tbPermissions = permissionService.list(new QueryWrapper<TbPermission>().in("permission_id", partEmployeeDto.getPermissionIds()));
            List<TbPartPermission> tbPartPermissions = partPermissionService.list(new QueryWrapper<TbPartPermission>().eq("part_id", partEmployeeDto.getPartId()).in("permission_id", partEmployeeDto.getPermissionIds()));
            if (CollUtil.isNotEmpty(tbPartPermissions)) {
                return R.fail(412, "存在重复的权限");
            }
            if (CollUtil.isNotEmpty(partEmployeeDto.getPermissionIds())) {
                for (Long permissionId : partEmployeeDto.getPermissionIds()) {
                    TbPartPermission tbPartPermission = new TbPartPermission();
                    tbPartPermission.setPermissionId(permissionId);
                    tbPartPermission.setPartId(partEmployeeDto.getPartId());
                    tbPartPermission.setCreateTime(new Date());
                    list.add(tbPartPermission);
                }
            }
            if (CollUtil.isNotEmpty(list)) {
                boolean save = partPermissionService.saveBatch(list);
                if (save) {
                    tokenRedisUtil.updateMenu(partEmployeeDto.getPartId(), tbPermissions
                    );
                    return R.ok(200, "操作成功", "");
                } else {
                    return R.fail(408, "添加失败");
                }
            }
            return R.fail(408, "添加失败");
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * 批量删除角色
     */
    @PostMapping(value = "/delPart")
    public R<Object> delPart(HttpServletRequest request,
                             @RequestParam(value = "partIds") List<Long> partIds) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            Integer count = partService.delPart(partIds);
            if (count > 0) {
                return R.ok(200, "删除成功");
            }
            return R.fail(413, "删除失败");
        } else {
            return R.fail(401, "请先登录");
        }
    }

    /**
     * 修改角色状态
     */
    @PostMapping(value = "/updateStatus")
    public R<Object> updateStatus(HttpServletRequest request, @RequestBody PartStatusDto partStatusDto) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            TbPart one = partService.getOne(new QueryWrapper<TbPart>().eq("part_id", partStatusDto.getPartId()));
            if (ObjectUtil.isNull(one)) {
                return R.fail(422, "要修改的角色不存在");
            }
            TbPart part = new TbPart();
            part.setPartId(partStatusDto.getPartId());
            part.setIsUse(partStatusDto.getIsUse());
            boolean update = partService.update(part, new QueryWrapper<TbPart>().eq("part_id", partStatusDto.getPartId()));
            if (update) {
                return R.ok(200, "修改成功");
            }
            return R.fail(423, "修改失败");
        } else {
            return R.fail(401, "请先登录");
        }

    }

    /**
     * 给用户分配角色
     */
    @PostMapping(value = "/addUserPart")
    public R<Object> addUserPart(HttpServletRequest request, @RequestBody UserPartDto userPartDto) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            //判断分配的角色是否重复
            List<TbEmployeePart> list = new ArrayList<>();
            List<TbEmployeePart> employeeParts = employeePartService.list(new QueryWrapper<TbEmployeePart>().eq("employee_id", userPartDto.getAdminId()).in("part_id", userPartDto.getRoleIds()));
            if (CollUtil.isNotEmpty(employeeParts)) {
                return R.fail(424, "存在重复的角色");
            }
            for (Long roleId : userPartDto.getRoleIds()) {
                TbEmployeePart employeePart = new TbEmployeePart();
                employeePart.setPartId(roleId);
                employeePart.setEmployeeId(userPartDto.getAdminId());
                employeePart.setCreateTime(new Date());
                list.add(employeePart);
            }
            if (CollUtil.isNotEmpty(list)) {
                boolean b = employeePartService.saveBatch(list);
                if (b) {
                    List<TbPart> parts = partService.list(new QueryWrapper<TbPart>().in("part_id", userPartDto.getRoleIds()));
                    tokenRedisUtil.editRole(parts, userPartDto.getAdminId());
                    return R.ok(200, "分配角色成功", "");
                }
                return R.fail(425, "分配角色失败");
            }
            return R.fail(425, "分配角色失败");

        } else {
            return R.fail(401, "请先登录");
        }

    }
}
