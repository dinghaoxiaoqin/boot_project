package com.rrk.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.manage.dao.TbPermissionMapper;
import com.rrk.common.modules.manage.dto.ParentMenuDto;
import com.rrk.common.modules.manage.entity.TbPermission;
import com.rrk.manage.service.ITbPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-04-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbPermissionServiceImpl extends ServiceImpl<TbPermissionMapper, TbPermission> implements ITbPermissionService {


    @Autowired
    private ITbPermissionService permissionService;

    /**
     * 获取权限列表
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    @Override
    public IPage<TbPermission> getPermissionList(Integer pageNo, Integer pageSize, String keyword) {
        log.info("传入的参数：pageNo->{},pageSize->{},keyword->{}",pageNo,pageSize,keyword);
         Page<TbPermission> page = new Page<>(pageNo, pageSize);
        if (StrUtil.isNotBlank(keyword)) {
            IPage<TbPermission> permissionIPage = permissionService.page(page, new QueryWrapper<TbPermission>().like("permission_name", keyword));
            return permissionIPage;
        } else {
             IPage<TbPermission> permissionIPage = permissionService.page(page);
           return permissionIPage;
        }
    }

    /**
     * 获取父级菜单数据
     * @param parentId
     * @return
     */
    @Override
    public List<ParentMenuDto> getParentMenus(Integer parentId) {
        List<ParentMenuDto> dtos = new ArrayList<>();
            List<TbPermission> list = permissionService.list();
            if (CollUtil.isNotEmpty(list)) {
                for (TbPermission tbPermission : list) {
                    ParentMenuDto menuDto = new ParentMenuDto();
                    menuDto.setParentId(tbPermission.getPermissionId());
                    menuDto.setParentName(tbPermission.getPermissionName());
                    dtos.add(menuDto);
                }
            }
        return dtos;
    }
}
