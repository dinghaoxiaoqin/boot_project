package com.rrk.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.user.dao.TbRegionMapper;
import com.rrk.common.modules.user.dto.webdto.CityDto;
import com.rrk.common.modules.user.dto.webdto.DistrictDto;
import com.rrk.common.modules.user.dto.webdto.ProvinceDto;
import com.rrk.common.modules.user.dto.webdto.RegionDto;
import com.rrk.common.modules.user.entity.TbRegion;
import com.rrk.user.service.ITbRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class TbRegionServiceImpl extends ServiceImpl<TbRegionMapper, TbRegion> implements ITbRegionService {

    @Autowired
    private ITbRegionService regionService;

    @Override
    public RegionDto getRegionList() {
        //1.获取省的信息
        List<TbRegion> provinceList = regionService.list(new QueryWrapper<TbRegion>().eq("level", 1));
        //2.获取市的数据
        List<TbRegion> cityList = regionService.list(new QueryWrapper<TbRegion>().eq("level", 2));
        //3.获取区（县）的数据
        List<TbRegion> districtList = regionService.list(new QueryWrapper<TbRegion>().eq("level", 3));
        //4,数据处理
        RegionDto regionDto = getHandle(provinceList, cityList, districtList);
        return regionDto;
    }

    /**
     * 处理数据
     *
     * @param provinceList
     * @param cityList
     * @param districtList
     * @return
     */
    private RegionDto getHandle(List<TbRegion> provinceList, List<TbRegion> cityList, List<TbRegion> districtList) {
        RegionDto regionDto = new RegionDto();
        List<ProvinceDto> provinceDtos = new ArrayList<>();
        for (TbRegion region : provinceList) {
            ProvinceDto provinceDto = new ProvinceDto();
            provinceDto.setCode(region.getId());
            provinceDto.setName(region.getName());
            provinceDto.setParentCode(0);
            provinceDtos.add(provinceDto);
        }
        List<CityDto> cityDtos = new ArrayList<>();
        for (TbRegion tbRegion : cityList) {
            CityDto cityDto = new CityDto();
            cityDto.setCode(tbRegion.getId());
            cityDto.setName(tbRegion.getName());
            cityDtos.add(cityDto);
        }
        List<DistrictDto> districtDtos = new ArrayList<>();
        for (TbRegion tbRegion : districtList) {
            DistrictDto districtDto = new DistrictDto();
            districtDto.setCode(tbRegion.getId());
            districtDto.setName(tbRegion.getName());
            districtDtos.add(districtDto);
        }
        regionDto.setProvinceDtoList(provinceDtos);
        regionDto.setCityDtoList(cityDtos);
        regionDto.setDistrictDtoList(districtDtos);
        return regionDto;
    }
}
