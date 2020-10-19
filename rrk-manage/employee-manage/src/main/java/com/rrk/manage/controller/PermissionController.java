package com.rrk.manage.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.manage.dto.ParentMenuDto;
import com.rrk.common.modules.manage.dto.PartPermissionDto;
import com.rrk.common.modules.manage.entity.TbPermission;
import com.rrk.common.utils.JwtTokenUtil;
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
 * 权限管理
 */
@RestController
@RequestMapping("/manage")
@CrossOrigin
@Slf4j
public class PermissionController {

    @Autowired
    private ITbPermissionService permissionService;

    @Autowired
    private TokenRedisUtil tokenRedisUtil;

    /**
     * 添加权限配置信息（只有超级管理员才有这个权限）
     */
    @PostMapping(value = "/addPermission")
    public R<Object> addPermission(HttpServletRequest request,
                                   @RequestBody TbPermission permission) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            //判断是否已经添加了
            TbPermission per = permissionService.getOne(new QueryWrapper<TbPermission>().eq("permission_id", permission.getPermissionId()));
            if (ObjectUtil.isNotNull(per)) {
                return R.fail(414, "该菜单已存在");
            }
            permission.setCreateTime(new Date());
            permission.setIsUse(0);
            boolean save = permissionService.save(permission);
            if (save) {
              //  tokenRedisUtil.updateMenu(permission);
                return R.ok(200, "操作成功", "");
            }
            return R.fail(407, "添加权限失败");
        } else {
            return R.fail(401, "未登录，请先登录");
        }
    }

    /**
     * 获取权限列表
     */
    @GetMapping(value = "/getPermissionList")
    public R<Object> getPermissionList(HttpServletRequest request,
                                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "keyword", required = false) String keyword) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            IPage<TbPermission> page = permissionService.getPermissionList(pageNo, pageSize, keyword);
            return R.ok(200, "操作成功", page);
        } else {
            return R.fail(401, "未登录，请先登录");
        }
    }

    /**
     * 获取父级菜单数据
     */
    @GetMapping(value = "/getParentMenus")
    public R<Object> getParentMenus(HttpServletRequest request,
                                    @RequestParam(value = "parentId", required = false) Integer parentId,
                                    @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize

    ) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            List<ParentMenuDto> list = permissionService.getParentMenus(parentId);
            return R.ok(200, "操作成功", list);
        } else {
            return R.fail(401, "未登录，请先登录");
        }
    }

    /**
     * 修改菜单
     */
    @PostMapping(value = "/updatePermission")
    public R<Object> updatePermission(HttpServletRequest request,
                                      @RequestBody TbPermission permission) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {

            TbPermission per = permissionService.getOne(new QueryWrapper<TbPermission>().eq("permission_id", permission.getPermissionId()));
            if (ObjectUtil.isNull(per)) {
                return R.fail(413, "菜单不存在");
            } else {
                permission.setUpdateTime(new Date());
                permission.setName(permission.getPermissionName());
            }
            boolean update = permissionService.update(permission, new QueryWrapper<TbPermission>().eq("permission_id", permission.getPermissionId()));
            if (update) {
               // tokenRedisUtil.updateMenu(permission);
                return R.ok(200, "修改成功", "");
            }
            return R.fail(407, "添加权限失败");
        } else {
            return R.fail(401, "未登录，请先登录");
        }

    }

    /**
     * 根据id获取要修改的菜单数据
     */
    @GetMapping(value = "/menu")
    public R<Object> menu(HttpServletRequest request,
                          @RequestParam(value = "permissionId", required = true) Integer permissionId) {
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            TbPermission tbPermission = permissionService.getOne(new QueryWrapper<TbPermission>().eq("permission_id", permissionId));
            return R.ok(200, "操作成功", tbPermission);
        } else {
            return R.fail(401, "未登录，请先登录");
        }

    }

    /**
     * 获取全部菜单
     */
    @GetMapping(value = "/getMenus")
    public R<Object> getMenus(HttpServletRequest request) {
        List<PartPermissionDto> dtos = new ArrayList<>();
        Long employeeId = JwtTokenUtil.getUserId(request);
        if (employeeId != null) {
            List<TbPermission> list = permissionService.list();
            if (CollUtil.isNotEmpty(list)) {
                for (TbPermission tbPermission : list) {
                    if (tbPermission.getParentId().longValue() == 0) {
                        PartPermissionDto dto = new PartPermissionDto();
                        dto.setPermissionId(tbPermission.getPermissionId());
                        dto.setPermissionName(tbPermission.getPermissionName());
                        dtos.add(dto);
                    }
                }
                dtos = handle(list, dtos);
            }
            return R.ok(200, "操作成功", dtos);
        } else {
            return R.fail(401, "未登录，请先登录");
        }

    }

    private List<PartPermissionDto> handle(List<TbPermission> list, List<PartPermissionDto> dtos) {
        if (CollUtil.isNotEmpty(dtos)) {
            for (PartPermissionDto dto : dtos) {
                List<PartPermissionDto> dtoList = new ArrayList<>();
                List<TbPermission> collect = list.stream().filter(l -> dto.getPermissionId().longValue() == l.getParentId().longValue())
                        .collect(Collectors.toList());
                if (CollUtil.isNotEmpty(collect)) {
                    for (TbPermission tbPermission : collect) {
                        PartPermissionDto permissionDto = new PartPermissionDto();
                        permissionDto.setPermissionId(tbPermission.getPermissionId());
                        permissionDto.setPermissionName(tbPermission.getPermissionName());
                        dtoList.add(permissionDto);
                    }
                }
                dto.setChildren(dtoList);
                handle(list,dtoList);
            }
        }
        return dtos;
    }

}
