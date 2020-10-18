package com.rrk.common.modules.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.manage.dto.EmployeeVo;
import com.rrk.common.modules.manage.entity.TbEmployee;
import com.rrk.common.modules.manage.entity.TbPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-04-25
 */
public interface TbEmployeeMapper extends BaseMapper<TbEmployee> {

    List<EmployeeVo> getEmployeeList();

    List<TbPermission> getPermissionsByUser(@Param("employeeId") Long employeeId);
}
