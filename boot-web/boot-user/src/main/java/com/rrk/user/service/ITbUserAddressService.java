package com.rrk.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.user.dto.webdto.AddressDto;
import com.rrk.common.modules.user.dto.webdto.AddressInfo;
import com.rrk.common.modules.user.dto.webdto.UserAddressDto;
import com.rrk.common.modules.user.entity.TbUserAddress;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-01
 */
public interface ITbUserAddressService extends IService<TbUserAddress> {

    /**
     * 获取用户默认的收货地址
     * @param userId
     * @return
     */
    UserAddressDto getDefaultAddress(Long userId);

    /**
     * 添加用户地址
     * @param addressInfo
     * @return
     */
    Integer addAddress(Long userId,AddressInfo addressInfo);

    /**
     * 获取用户地址列表
     * @param userId
     * @return
     */
    List<AddressDto> getAddressList(Long userId);

    /**
     * 用户选择收货地址
     * @param userId
     * @param id
     * @return
     */
    Integer selectAddress(Long userId, Long id);
}
