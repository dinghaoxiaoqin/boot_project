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
@TableName("tb_platform_activity")
public class TbPlatformActivity extends Model<TbPlatformActivity> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动过期时间
     */
    private Date overTime;

    /**
     * 活动结束（0否，1是）
     */
    private Integer isActivity;
    /**
     * 活动说明activityExplain
     */
    private String activityExplain;

    /**
     * 筹单/满减阈值
     */
    private Integer nums;

    /**
     * 是否是满减
     */
    private Integer isGift;

    /**
     * man_amount 满多少钱
     */
    private BigDecimal manAmount;

    /**
     * giftAmount 满减的金额
     */
    private BigDecimal giftAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;




    public Long getId() {
        return id;
    }

    public TbPlatformActivity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getActivityName() {
        return activityName;
    }

    public TbPlatformActivity setActivityName(String activityName) {
        this.activityName = activityName;
        return this;
    }

    public Date getOverTime() {
        return overTime;
    }

    public TbPlatformActivity setOverTime(Date overTime) {
        this.overTime = overTime;
        return this;
    }

    public Integer getIsActivity() {
        return isActivity;
    }

    public TbPlatformActivity setIsActivity(Integer isActivity) {
        this.isActivity = isActivity;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbPlatformActivity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbPlatformActivity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getActivityExplain() {
        return activityExplain;
    }

    public TbPlatformActivity setActivityExplain(String activityExplain) {
        this.activityExplain = activityExplain;
        return this;
    }

    public Integer getNums() {
        return nums;
    }

    public TbPlatformActivity setNums(Integer nums) {
        this.nums = nums;
        return this;
    }

    public Integer getIsGift() {
        return isGift;
    }

    public TbPlatformActivity setIsGift(Integer isGift) {
        this.isGift = isGift;
        return this;
    }

    public BigDecimal getManAmount() {
        return manAmount;
    }

    public TbPlatformActivity setManAmount(BigDecimal manAmount) {
        this.manAmount = manAmount;
        return this;
    }

    public BigDecimal getGiftAmount() {
        return giftAmount;
    }

    public TbPlatformActivity setGiftAmount(BigDecimal giftAmount) {
        this.giftAmount = giftAmount;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    @Override
    public String toString() {
        return "TbPlatformActivity{" +
        "id=" + id +
        ", activityName=" + activityName +
        ", overTime=" + overTime +
        ", isActivity=" + isActivity +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
