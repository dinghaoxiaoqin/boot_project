package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@TableName("tb_cart")
public class TbCart extends Model<TbCart> {

private static final long serialVersionUID=1L;

    /**
     * sku的在购物车中数量最大值
     */
    public static  final  Integer MAX_COUNT = 5;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 购物车商品上限数量
     */
    private Integer productNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public TbCart setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public TbCart setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Integer getProductNum() {
        return productNum;
    }

    public TbCart setProductNum(Integer productNum) {
        this.productNum = productNum;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbCart setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbCart setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbCart{" +
        "id=" + id +
        ", userId=" + userId +
        ", productNum=" + productNum +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
