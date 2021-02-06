package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * <p>
 * 商品分类和品牌的中间表，两者是多对多关系
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@TableName("tb_category_brand")
public class TbCategoryBrand extends Model<TbCategoryBrand> implements Serializable{

private static final long serialVersionUID=1L;


    /**
     * 商品类目id
     */

    private Long categoryId;

    /**
     * 品牌id
     */
    private Long brandId;

    public TbCategoryBrand() {
    }

    public TbCategoryBrand(Long id, Long categoryId) {
        this.brandId = id;
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public TbCategoryBrand setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Long getBrandId() {
        return brandId;
    }

    public TbCategoryBrand setBrandId(Long brandId) {
        this.brandId = brandId;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.categoryId;
    }

    @Override
    public String toString() {
        return "TbCategoryBrand{" +
        "categoryId=" + categoryId +
        ", brandId=" + brandId +
        "}";
    }
}
