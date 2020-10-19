package com.rrk.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.user.dao.TbUserMapper;
import com.rrk.common.modules.user.dto.webdto.UserDto;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.user.service.ITbUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-09
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {

    @Autowired
    private ITbUserService userService;

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    @Override
    public TbUser getUserById(Long userId) {
        TbUser user = userService.getOne(new QueryWrapper<TbUser>().eq("user_id", userId));
        return user;
    }

    /**
     * 编辑用户信息
     * @param userDto
     * @return
     */
    @Override
    public Integer editUser(UserDto userDto) {
        TbUser tbUser = userService.getOne(new QueryWrapper<TbUser>().eq("user_id", userDto.getUserId()));
        if (ObjectUtil.isNull(tbUser)) {
            throw new MyException(401,"该用户不存在");
        }
        tbUser.setUserSrc(userDto.getUserSrc());
        tbUser.setUserRank(userDto.getUserRank());
        tbUser.setUpdateTime(new Date());
        tbUser.setNickName(userDto.getNickName());
        boolean b = userService.updateById(tbUser);
        return b == true? 1:0 ;
    }
}

