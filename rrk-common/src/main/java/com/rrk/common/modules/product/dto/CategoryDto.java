package com.rrk.common.modules.product.dto;

import com.rrk.common.modules.product.entity.TbCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CategoryDto implements Serializable {

    /**
     * 分类id
     */
    private Long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 子级数据
     */
    private List<CategoryDto> childList;

    public CategoryDto() {

    }

    public CategoryDto(TbCategory tbCategory) {
        this.id = tbCategory.getId();
        this.name = tbCategory.getName();

    }
}
