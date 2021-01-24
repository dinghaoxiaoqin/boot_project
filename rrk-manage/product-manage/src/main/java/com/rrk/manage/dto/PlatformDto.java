package com.rrk.manage.dto;

import com.rrk.common.modules.product.entity.TbPlatformActivity;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlatformDto extends TbPlatformActivity implements Serializable {

    private String overtime;

    private String createtime;
}
