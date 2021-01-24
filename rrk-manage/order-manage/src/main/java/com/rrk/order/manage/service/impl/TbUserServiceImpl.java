package com.rrk.order.manage.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.user.dao.TbUserMapper;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.order.manage.service.ITbUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-09
 */
@Service
@DS("boot")
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {


}

