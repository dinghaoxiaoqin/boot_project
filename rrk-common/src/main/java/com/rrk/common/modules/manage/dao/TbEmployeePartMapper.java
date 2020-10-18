package com.rrk.common.modules.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.manage.entity.TbEmployeePart;
import com.rrk.common.modules.manage.entity.TbPart;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-04-25
 */
public interface TbEmployeePartMapper extends BaseMapper<TbEmployeePart> {

    /**
     * 根据用户id 获取用户角色信息
     * @param employeeId
     * @return
     */
    List<TbPart> getPartByEmployeeId(@Param("employeeId") Long employeeId);
}
