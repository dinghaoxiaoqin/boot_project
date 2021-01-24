package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * sku表,该表表示具体的商品实体,如黑色的 64g的iphone 8
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@TableName("tb_sku")
public class TbSku extends Model<TbSku> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * sku id
     */
   // @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * spu id
     */
    private Long spuId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品的图片，多个图片以‘,’分割
     */
    private String images;

    /**
     * 商品原价，单位为分
     */
    private BigDecimal price;

    private BigDecimal salePrice;
    /**
     * 是否参与会员打折（0否 1是）
     */
    private Integer isVip;

    /**
     * 特有规格属性在spu属性模板中的对应下标组合
     */
    private String indexes;

    /**
     * 商品库存
     */
    private Integer stock;

    /**
     * 商品销量
     */
    private Integer saleCount;

    /**
     * sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序
     */
    private String ownSpec;

    /**
     * 是否有效，0无效，1有效
     */
    private Integer enable;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public TbSku setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSpuId() {
        return spuId;
    }

    public TbSku setSpuId(Long spuId) {
        this.spuId = spuId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TbSku setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImages() {
        return images;
    }

    public TbSku setImages(String images) {
        this.images = images;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public TbSku setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getIndexes() {
        return indexes;
    }

    public TbSku setIndexes(String indexes) {
        this.indexes = indexes;
        return this;
    }

    public Integer getStock() {
        return stock;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public TbSku setStock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public TbSku setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
        return this;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public TbSku setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
        return this;
    }

    public Integer getEnable() {
        return enable;
    }

    public TbSku setEnable(Integer enable) {
        this.enable = enable;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbSku setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbSku setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbSku{" +
        "id=" + id +
        ", spuId=" + spuId +
        ", title=" + title +
        ", images=" + images +
        ", price=" + price +
        ", salePrice=" + salePrice +
        ", isVip=" + isVip +
        ", indexes=" + indexes +
        ", stock=" + stock +
        ", saleCount=" + saleCount +
        ", ownSpec=" + ownSpec +
        ", enable=" + enable +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
