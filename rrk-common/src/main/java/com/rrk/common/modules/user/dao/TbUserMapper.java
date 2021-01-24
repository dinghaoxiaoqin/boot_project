package com.rrk.common.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rrk.common.modules.user.dto.UserMenuDto;
import com.rrk.common.modules.user.entity.TbUser;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author dinghao
 * @since 2020-02-08
 */
public interface TbUserMapper extends BaseMapper<TbUser> {

    List<UserMenuDto> getMenuByUserName(@Param("username") String username);



    TbUser selectByUserId(@Param("id") long id);
}
