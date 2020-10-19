package com.rrk.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.user.dto.webdto.RegionDto;
import com.rrk.common.modules.user.entity.TbRegion;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-01
 */
public interface ITbRegionService extends IService<TbRegion> {

    /**
     * 获取省市区（县）的数据
     * @return
     */
    RegionDto getRegionList();
}
