package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * spu表，该表描述的是一个抽象性的商品，比如 iphone8
 * </p>
 *
 * @author dinghao
 * @since 2020-06-25
 */
@TableName("tb_spu")
public class TbSpu extends Model<TbSpu> implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * spu id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 子标题
     */
    private String subTitle;

    /**
     * 1级类目id
     */
    private Long cid1;

    /**
     * 2级类目id
     */
    private Long cid2;

    /**
     * 3级类目id
     */
    private Long cid3;

    /**
     * 商品所属品牌id
     */
    private Long brandId;

    /**
     * 是否上架，0下架，1上架
     */
    private Integer saleable;

    /**
     * 是否有效，0已删除，1有效
     */
    private Integer valid;

    /**
     * 商品描述
     */
    private String spuDescrition;

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

    public TbSpu setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TbSpu setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public TbSpu setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public Long getCid1() {
        return cid1;
    }

    public TbSpu setCid1(Long cid1) {
        this.cid1 = cid1;
        return this;
    }

    public Long getCid2() {
        return cid2;
    }

    public TbSpu setCid2(Long cid2) {
        this.cid2 = cid2;
        return this;
    }

    public Long getCid3() {
        return cid3;
    }

    public TbSpu setCid3(Long cid3) {
        this.cid3 = cid3;
        return this;
    }

    public Long getBrandId() {
        return brandId;
    }

    public TbSpu setBrandId(Long brandId) {
        this.brandId = brandId;
        return this;
    }

    public Integer getSaleable() {
        return saleable;
    }

    public TbSpu setSaleable(Integer saleable) {
        this.saleable = saleable;
        return this;
    }

    public Integer getValid() {
        return valid;
    }

    public TbSpu setValid(Integer valid) {
        this.valid = valid;
        return this;
    }

    public String getSpuDescrition() {
        return spuDescrition;
    }

    public TbSpu setSpuDescrition(String spuDescrition) {
        this.spuDescrition = spuDescrition;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbSpu setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbSpu setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbSpu{" +
        "id=" + id +
        ", title=" + title +
        ", subTitle=" + subTitle +
        ", cid1=" + cid1 +
        ", cid2=" + cid2 +
        ", cid3=" + cid3 +
        ", brandId=" + brandId +
        ", saleable=" + saleable +
        ", valid=" + valid +
        ", spuDescrition=" + spuDescrition +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
