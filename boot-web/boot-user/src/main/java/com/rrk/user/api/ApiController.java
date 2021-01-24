package com.rrk.user.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.modules.user.dto.webdto.RegionDto;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.user.service.ITbRegionService;
import com.rrk.user.service.ITbUserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 提供对外访问的接口
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
@Slf4j
public class ApiController {

    @Autowired
    private ITbUserAddressService userAddressService;

    @Autowired
    private ITbRegionService regionService;



    /**
     * 根据用户id和地址id获取地址信息
     * @return
     */
    @GetMapping(value = "/getUserAddressById")
    public TbUserAddress getUserAddressById(@RequestParam(value = "userId") Long userId,
                                            @RequestParam(value = "addressId") Long addressId){
        TbUserAddress userAddress = userAddressService.getOne(new QueryWrapper<TbUserAddress>().eq("id", addressId).eq("user_id", userId));
        return userAddress;

    }

    /**
     * 获取所有的省市区信息
     */
    @GetMapping(value = "/getRegions")
    public RegionDto getRegions(){
        RegionDto regionList = regionService.getRegionList();
        return regionList;
    }
}
