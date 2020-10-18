package com.rrk.common.modules.user.dto.webdto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 省市区（县返回的实体）
 */
@Data
public class RegionDto implements Serializable {

    /**
     * 省
     */
    private List<ProvinceDto> provinceDtoList;
    /**
     * 市
     */
    private List<CityDto> cityDtoList;
    /**
     * 区（县）
     */
    private List<DistrictDto> districtDtoList;



    
}
