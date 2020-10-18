package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@TableName("tb_category")
@Data
public class TbCategory extends Model<TbCategory> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 类目id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类目名称
     */
    private String name;

    /**
     * 父类目id,顶级类目填0
     */
    private Long parentId;

    /**
     * 是否为父节点，0为否，1为是
     */
    private Integer isParent;

    /**
     * 排序指数，越小越靠前
     */
    private Integer sort;

   // private Long pidId;


    public Long getId() {
        return id;
    }

    public TbCategory setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TbCategory setName(String name) {
        this.name = name;
        return this;
    }

    public Long getParentId() {
        return parentId;
    }

    public TbCategory setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public TbCategory setIsParent(Integer isParent) {
        this.isParent = isParent;
        return this;
    }

    public Integer getSort() {
        return sort;
    }

    public TbCategory setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbCategory{" +
        "id=" + id +
        ", name=" + name +
        ", parentId=" + parentId +
        ", isParent=" + isParent +
        ", sort=" + sort +
        "}";
    }
}
