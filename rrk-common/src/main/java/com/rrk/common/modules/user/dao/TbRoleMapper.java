package com.rrk.common.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.user.dto.SystemRoleDto;
import com.rrk.common.modules.user.entity.TbRole;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-02-08
 */
public interface TbRoleMapper extends BaseMapper<TbRole> {

    /**
     * 获取角色列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<SystemRoleDto> getRoleList(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
}
