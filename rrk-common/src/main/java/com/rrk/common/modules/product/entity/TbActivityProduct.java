package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@TableName("tb_activity_product")
public class TbActivityProduct extends Model<TbActivityProduct> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 商品spuid
     */
    private Long spuId;

    /**
     * 商品skuid
     */
    private Long skuId;

    /**
     * 商品sku名称
     */
    private String skuName;

    /**
     * 商品sku的图片url
     */
    private String skuImage;

    /**
     * 商品sku的描述
     */
    private String skuDesc;

    /**
     * 商品sku活动价格
     */
    private BigDecimal activityPrice;

    /**
     * 参加活动库存
     */
    private Integer activityStock;

    /**
     * 是否售罄（0否，1是）
     */
    private Integer isSell;

    /**
     * 活动结束（0否，1是）
     */
    private Integer isActivity;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 秒杀商品开始时间
     * @return
     */
    private Date startTime;

    /**
     * 秒杀商品结束时间
     * @return
     */
    private Date endTime;


    public Long getId() {
        return id;
    }

    public TbActivityProduct setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getActivityId() {
        return activityId;
    }

    public TbActivityProduct setActivityId(Long activityId) {
        this.activityId = activityId;
        return this;
    }

    public Long getSpuId() {
        return spuId;
    }

    public TbActivityProduct setSpuId(Long spuId) {
        this.spuId = spuId;
        return this;
    }

    public Long getSkuId() {
        return skuId;
    }

    public TbActivityProduct setSkuId(Long skuId) {
        this.skuId = skuId;
        return this;
    }

    public String getSkuName() {
        return skuName;
    }

    public TbActivityProduct setSkuName(String skuName) {
        this.skuName = skuName;
        return this;
    }

    public String getSkuImage() {
        return skuImage;
    }

    public TbActivityProduct setSkuImage(String skuImage) {
        this.skuImage = skuImage;
        return this;
    }

    public String getSkuDesc() {
        return skuDesc;
    }

    public TbActivityProduct setSkuDesc(String skuDesc) {
        this.skuDesc = skuDesc;
        return this;
    }

    public BigDecimal getActivityPrice() {
        return activityPrice;
    }

    public TbActivityProduct setActivityPrice(BigDecimal activityPrice) {
        this.activityPrice = activityPrice;
        return this;
    }

    public Integer getActivityStock() {
        return activityStock;
    }

    public TbActivityProduct setActivityStock(Integer activityStock) {
        this.activityStock = activityStock;
        return this;
    }

    public Integer getIsSell() {
        return isSell;
    }

    public TbActivityProduct setIsSell(Integer isSell) {
        this.isSell = isSell;
        return this;
    }

    public Integer getIsActivity() {
        return isActivity;
    }

    public TbActivityProduct setIsActivity(Integer isActivity) {
        this.isActivity = isActivity;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbActivityProduct setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbActivityProduct setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbActivityProduct{" +
        "id=" + id +
        ", activityId=" + activityId +
        ", spuId=" + spuId +
        ", skuId=" + skuId +
        ", skuName=" + skuName +
        ", skuImage=" + skuImage +
        ", skuDesc=" + skuDesc +
        ", activityPrice=" + activityPrice +
        ", activityStock=" + activityStock +
        ", isSell=" + isSell +
        ", isActivity=" + isActivity +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        "}";
    }
}
