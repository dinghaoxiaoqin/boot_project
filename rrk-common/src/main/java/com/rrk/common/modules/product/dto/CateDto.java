package com.rrk.common.modules.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
@Data
public class CateDto implements Serializable {

    private List<Long> ids;
}
