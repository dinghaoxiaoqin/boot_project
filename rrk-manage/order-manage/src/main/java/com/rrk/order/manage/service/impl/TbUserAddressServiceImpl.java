package com.rrk.order.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.user.dao.TbUserAddressMapper;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.order.manage.config.dynamicDataSource.DBTypeEnum;
import com.rrk.order.manage.config.dynamicDataSource.DataSource;
import com.rrk.order.manage.service.ITbUserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbUserAddressServiceImpl extends ServiceImpl<TbUserAddressMapper, TbUserAddress> implements ITbUserAddressService {

    @Autowired
    private ITbUserAddressService addressService;

    @DataSource(DBTypeEnum.boot)
    @Override
    public TbUserAddress getAddressById(Long addressId) {
        return addressService.getOne(new QueryWrapper<TbUserAddress>().eq("id", addressId));
    }
}
