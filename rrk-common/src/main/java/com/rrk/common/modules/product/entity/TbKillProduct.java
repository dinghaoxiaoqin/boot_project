package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rrk.common.modules.product.dto.KillProductDto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author dinghao
 * @since 2020-04-16
 */
@Data
public class TbKillProduct extends Model<TbKillProduct> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 秒杀商品图片
     */
    private String productUrl;

    /**
     * 秒杀后剩余库存
     */
    private Integer productKillStock;

    /**
     * 秒杀初始库存
     */
    private Integer productInitStock;

    /**
     * 原售价
     */
    private BigDecimal productPrice;

    /**
     * 秒杀价
     */
    private BigDecimal productKillPrice;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public TbKillProduct() {
    }

    public TbKillProduct(KillProductDto killProductDto,TbProduct product) {
        this.setCreateTime(new Date());
        this.setProductId(killProductDto.getProductId());
        this.setProductInitStock(killProductDto.getProductKillStock());
        this.setProductName(killProductDto.getProductName());
        this.setProductKillPrice(killProductDto.getProductKillPrice());
        this.setProductPrice(killProductDto.getProductPrice());
        this.setProductUrl(killProductDto.getProductUrl());
    }

    public Long getId() {
        return id;
    }

    public TbKillProduct setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public TbKillProduct setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public TbKillProduct setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public TbKillProduct setProductUrl(String productUrl) {
        this.productUrl = productUrl;
        return this;
    }

    public Integer getProductKillStock() {
        return productKillStock;
    }

    public TbKillProduct setProductKillStock(Integer productKillStock) {
        this.productKillStock = productKillStock;
        return this;
    }

    public Integer getProductInitStock() {
        return productInitStock;
    }

    public TbKillProduct setProductInitStock(Integer productInitStock) {
        this.productInitStock = productInitStock;
        return this;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public TbKillProduct setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public BigDecimal getProductKillPrice() {
        return productKillPrice;
    }

    public TbKillProduct setProductKillPrice(BigDecimal productKillPrice) {
        this.productKillPrice = productKillPrice;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbKillProduct setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbKillProduct setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbKillProduct{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName=" + productName +
                ", productUrl=" + productUrl +
                ", productKillStock=" + productKillStock +
                ", productInitStock=" + productInitStock +
                ", productPrice=" + productPrice +
                ", productKillPrice=" + productKillPrice +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
