package com.rrk.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.user.dao.TbUserAddressMapper;
import com.rrk.common.modules.user.dto.webdto.AddressDto;
import com.rrk.common.modules.user.dto.webdto.AddressInfo;
import com.rrk.common.modules.user.dto.webdto.UserAddressDto;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.common.utils.ToolUtil;
import com.rrk.user.service.ITbUserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private ITbUserAddressService userAddressService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取用户默认的地址
     */
    @Override
    public UserAddressDto getDefaultAddress(Long userId) {
        TbUserAddress userAddress = userAddressService.getOne(new QueryWrapper<TbUserAddress>().eq("user_id", userId).eq("is_select", 1));
        if (ObjectUtil.isNotNull(userAddress)) {
            UserAddressDto userAddressDto = new UserAddressDto();
            userAddressDto.setAddress(userAddress.getUserAddress());
            userAddressDto.setIsApprove(userAddress.getIsApprove());
            userAddressDto.setUserId(userId);
            userAddressDto.setUserMobile(userAddress.getUserMobile());
            userAddressDto.setUserName(userAddress.getUserName());
            userAddressDto.setAddressId(userAddress.getId());
            return userAddressDto;
        }
        return null;
    }

    /**
     * 添加用户地址
     *
     * @param addressInfo
     * @return
     */
    @Override
    public Integer addAddress(Long userId, AddressInfo addressInfo) {
        TbUserAddress userAddress = new TbUserAddress(userId, addressInfo);
        Object o = redisTemplate.opsForHash().get(RedisConstant.USER_ADDRESS_KEY, userId.toString());
        List<TbUserAddress> userAddressList = new ArrayList<>();
        //判断是否修改
        TbUserAddress one = userAddressService.getOne(new QueryWrapper<TbUserAddress>().eq("id", addressInfo.getId()));
        if (ObjectUtil.isNotNull(one)) {
            userAddress.setId(one.getId());
            userAddressService.updateById(userAddress);
            userAddressList = JSON.parseArray(o.toString(), TbUserAddress.class);
            TbUserAddress address = userAddressList.stream()
                    .filter(u -> ToolUtil.equals(one.getId(), u.getId())).findFirst()
                    .orElse(null);
            Collections.replaceAll(userAddressList,address,one);
            redisTemplate.opsForHash().put(RedisConstant.USER_ADDRESS_KEY, userId.toString(), JSON.toJSONString(userAddressList));
            return 1;
        } else {
            boolean save = userAddressService.save(userAddress);
            if (save) {
                userAddressList = JSON.parseArray(o.toString(), TbUserAddress.class);
                userAddressList.add(userAddress);
                redisTemplate.opsForHash().put(RedisConstant.USER_ADDRESS_KEY, userId.toString(), JSON.toJSONString(userAddressList));
                return 1;
            }
        }
        return 0;
    }

    /**
     * 获取用户地址
     *
     * @param userId
     * @return
     */
    @Override
    public List<AddressDto> getAddressList(Long userId) {
        List<TbUserAddress> list = userAddressService.list(new QueryWrapper<TbUserAddress>().eq("user_id", userId));
        if (CollUtil.isNotEmpty(list)) {
            List<AddressDto> dtos = new ArrayList<>();
            for (TbUserAddress userAddress : list) {
                AddressDto addressDto = new AddressDto(userAddress);
                dtos.add(addressDto);
            }
            return dtos;
        }
        return null;
    }

    /**
     * 用户选择收货地址
     *
     * @param userId
     * @param id
     * @return
     */
    @Override
    public Integer selectAddress(Long userId, Long id) {
        Object o = redisTemplate.opsForHash().get(RedisConstant.USER_ADDRESS_KEY, userId.toString());
        TbUserAddress one = userAddressService.getOne(new QueryWrapper<TbUserAddress>().eq("user_id", userId).eq("id", id));
        if (ObjectUtil.isNull(one)) {
            throw new MyException(400, "获取用户要选中地址不存在");
        }
        one.setIsSelect(1);
        userAddressService.updateById(one);
        List<TbUserAddress> addressList = JSON.parseArray(o.toString(),TbUserAddress.class);
        TbUserAddress address = addressList.stream()
                .filter(u -> ToolUtil.equals(one.getId(), u.getId())).findFirst()
                .orElse(null);
        Collections.replaceAll(addressList,address,one);
        List<TbUserAddress> list = userAddressService.list(new QueryWrapper<TbUserAddress>().eq("user_id", userId).notIn("id", id));
        if (CollUtil.isNotEmpty(list)) {
            for (TbUserAddress userAddress : list) {
                userAddress.setIsSelect(0);
                TbUserAddress tbUserAddress = addressList.stream()
                        .filter(u -> ToolUtil.equals(userAddress.getId(), u.getId())).findFirst()
                        .orElse(null);
                if (ObjectUtil.isNotNull(tbUserAddress)) {
                    Collections.replaceAll(addressList,tbUserAddress,userAddress);
                }

            }
        }
        redisTemplate.opsForHash().put(RedisConstant.USER_ADDRESS_KEY, userId.toString(), JSON.toJSONString(addressList));
        userAddressService.updateBatchById(list);
        return 1;
    }

}
