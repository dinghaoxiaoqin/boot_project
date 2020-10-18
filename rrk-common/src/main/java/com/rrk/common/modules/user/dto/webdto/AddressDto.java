package com.rrk.common.modules.user.dto.webdto;

import com.rrk.common.modules.user.entity.TbUserAddress;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressDto implements Serializable {

    private String id;

    private String name;

    private String tel;

    private String address;

    private Boolean isDefault;

    private Integer isSelect;

    public AddressDto() {

    }

    public AddressDto(TbUserAddress userAddress) {
        this.id = userAddress.getId().toString();
        this.address = userAddress.getUserAddress();
        this.name = userAddress.getUserName();
        this.tel = userAddress.getUserMobile();
        this.isDefault = userAddress.getIsApprove() == 1 ? true : false;
        this.isSelect = userAddress.getIsSelect();
    }
}
