package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rrk.common.modules.product.dto.BrandDto;

import java.io.Serializable;

/**
 * <p>
 * 品牌表，一个品牌下有多个商品（spu），一对多关系
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@TableName("tb_brand")
public class TbBrand extends Model<TbBrand> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 品牌id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌图片地址
     */
    private String image;

    /**
     * 品牌的首字母
     */
    private String letter;

    public TbBrand() {
    }

    public TbBrand(BrandDto brandDto) {
        this.name = brandDto.getName();
        this.image = brandDto.getImage();
        this.letter = brandDto.getLetter();
    }

    public Long getId() {
        return id;
    }

    public TbBrand setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TbBrand setName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public TbBrand setImage(String image) {
        this.image = image;
        return this;
    }

    public String getLetter() {
        return letter;
    }

    public TbBrand setLetter(String letter) {
        this.letter = letter;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbBrand{" +
        "id=" + id +
        ", name=" + name +
        ", image=" + image +
        ", letter=" + letter +
        "}";
    }
}
