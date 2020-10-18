package com.rrk.common.modules.user.dto.webdto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class ProvinceDto implements Serializable {

    private Integer code;
    /**
     * 父级编号
     */
    private Integer parentCode;
    /**
     * 名称
     */
    private String name;
}
