package com.rrk.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.manage.dto.ParentMenuDto;
import com.rrk.common.modules.manage.entity.TbPermission;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-04-25
 */
public interface ITbPermissionService extends IService<TbPermission> {

    /**
     * 获取权限列表
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    IPage<TbPermission> getPermissionList(Integer pageNo, Integer pageSize, String keyword);

    /**
     * 获取父级菜单数据
     * @param parentId
     * @return
     */
    List<ParentMenuDto> getParentMenus(Integer parentId);
}
