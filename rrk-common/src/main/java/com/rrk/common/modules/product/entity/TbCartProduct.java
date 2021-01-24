package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rrk.common.modules.product.dto.webdto.AddCartDto;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 购物车关联商品表
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@TableName("tb_cart_product")
public class TbCartProduct extends Model<TbCartProduct> {

    public static final Long SHOP_ID = 0L;

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 购物车id
     */
    private Long cartId;

    /**
     * 商品spuId
     */
    private Long spuId;

    /**
     * 商品skuId
     */
    private Long skuId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * sku加入购物车数量
     */
    private Integer skuNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public TbCartProduct() {
    }

    public TbCartProduct(AddCartDto addCartDto, TbCart cart) {
        this.cartId = cart.getId();
        this.createTime  = new Date();
        this.shopId = addCartDto.getShopId();
        this.skuId = addCartDto.getSkuId();
        this.spuId = addCartDto.getSpuId();
        this.skuNum = addCartDto.getNums();
    }

    public Long getId() {
        return id;
    }

    public TbCartProduct setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getCartId() {
        return cartId;
    }

    public TbCartProduct setCartId(Long cartId) {
        this.cartId = cartId;
        return this;
    }

    public Long getSpuId() {
        return spuId;
    }

    public TbCartProduct setSpuId(Long spuId) {
        this.spuId = spuId;
        return this;
    }

    public Long getSkuId() {
        return skuId;
    }

    public TbCartProduct setSkuId(Long skuId) {
        this.skuId = skuId;
        return this;
    }

    public Long getShopId() {
        return shopId;
    }

    public TbCartProduct setShopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public TbCartProduct setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbCartProduct setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbCartProduct setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbCartProduct{" +
        "id=" + id +
        ", cartId=" + cartId +
        ", spuId=" + spuId +
        ", skuId=" + skuId +
        ", shopId=" + shopId +
        ", skuNum=" + skuNum +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
