package com.rrk.order.manage.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.user.entity.TbUserAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-01
 */
@Service
@DS("boot")
@Transactional(rollbackFor = Exception.class)
public interface ITbUserAddressService extends IService<TbUserAddress> {

}
