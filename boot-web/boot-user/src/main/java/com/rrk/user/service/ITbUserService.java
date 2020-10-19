package com.rrk.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.user.dto.webdto.UserDto;
import com.rrk.common.modules.user.entity.TbUser;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-09
 */
public interface ITbUserService extends IService<TbUser> {

    /**
     * 根据用户id 获取用户信息
     * @param userId
     * @return
     */
    TbUser getUserById(Long userId);

    /**
     *
     * @param userDto
     * @return
     */
    Integer editUser(UserDto userDto);
}
