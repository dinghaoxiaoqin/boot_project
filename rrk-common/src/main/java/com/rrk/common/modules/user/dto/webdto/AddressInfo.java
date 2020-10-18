package com.rrk.common.modules.user.dto.webdto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class AddressInfo implements Serializable {


    private Long id;

    private String name;

    private String tel;

    private String province;

    private String city;

    private String country;

    private String addressDetail;

    private Boolean isDefault;
}
