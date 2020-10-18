package com.rrk.common.modules.user.dto.webdto;

import com.rrk.common.modules.user.entity.TbUserAddress;
import lombok.Data;

import java.io.Serializable;

@Data
public class UdateAddressDto implements Serializable {

    private Long id;

    private String name;

    private String tel;

    private String province;

    private String city;

    private String country;

    private String addressDetail;

    private Boolean isDefault;

    public UdateAddressDto() {
    }

    public UdateAddressDto(TbUserAddress one) {
        this.id = one.getId();
        this.name = one.getUserName();
        this.province = one.getProvince();
        this.city = one.getCity();
        this.country = one.getDistrict();
        this.addressDetail = one.getUserAddress();
        this.tel = one.getUserMobile();
        this.isDefault = one.getIsApprove() == 1? true: false;
    }
}
