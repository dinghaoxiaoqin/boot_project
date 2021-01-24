package com.rrk.order.manage.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.user.dao.TbUserAddressMapper;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.order.manage.service.ITbUserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-01
 */
@Service
@DS("boot")
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbUserAddressServiceImpl extends ServiceImpl<TbUserAddressMapper, TbUserAddress> implements ITbUserAddressService {

}
