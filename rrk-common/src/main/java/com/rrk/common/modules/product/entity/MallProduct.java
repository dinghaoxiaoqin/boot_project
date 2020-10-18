package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dinghao
 * @since 2020-06-12
 */
@Data
@Setter
@Getter
public class MallProduct extends Model<MallProduct> implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类id,对应mall_category表的主键
     */
    private Integer categoryId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品副标题
     */
    private String subtitle;

    /**
     * 产品主图,url相对地址
     */
    private String mainImage;

    /**
     * 图片地址,json格式,扩展用
     */
    private String subImages;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 价格,单位-元保留两位小数
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 商品销量
     */
    private Integer saleCount;

    /**
     * 商品状态.1-在售 2-下架 3-删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public MallProduct setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public MallProduct setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getName() {
        return name;
    }

    public MallProduct setName(String name) {
        this.name = name;
        return this;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public MallProduct setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public String getMainImage() {
        return mainImage;
    }

    public MallProduct setMainImage(String mainImage) {
        this.mainImage = mainImage;
        return this;
    }

    public String getSubImages() {
        return subImages;
    }

    public MallProduct setSubImages(String subImages) {
        this.subImages = subImages;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public MallProduct setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MallProduct setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Integer getStock() {
        return stock;
    }

    public MallProduct setStock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public MallProduct setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MallProduct setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public MallProduct setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public MallProduct setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallProduct{" +
        "id=" + id +
        ", categoryId=" + categoryId +
        ", name=" + name +
        ", subtitle=" + subtitle +
        ", mainImage=" + mainImage +
        ", subImages=" + subImages +
        ", detail=" + detail +
        ", price=" + price +
        ", stock=" + stock +
        ", saleCount=" + saleCount +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
