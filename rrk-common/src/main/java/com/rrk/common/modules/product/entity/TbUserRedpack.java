package com.rrk.common.modules.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * @since 2020-03-08
 */
@Data
@TableName(value = "tb_user_redpack")
public class TbUserRedpack extends Model<TbUserRedpack> implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 抢红包的用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 抢发送红包的id
     */
    @TableField("red_pack_id")
    private Long redPackId;

    /**
     * 抢到的红包金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 抢红包的时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 版本控制
     */
    private Integer version;

//    public TbUserRedpack(UserRed userRed,TbRedPacket redPacket){
//        this.setUserId(userRed.getUserId());
//        this.setRedPackId(userRed.getRedId());
//        this.setAmount(redPacket.getUnitAmount());
//        this.setVersion(1);
//        this.setCreateTime(new Date());
//
//    }
//    public TbUserRedpack(Long userId, TbRedPacket tbRedPacket) {
//        this.setUserId(userId);
//        this.setRedPackId(new Random().nextLong());
//        this.setAmount(tbRedPacket.getUnitAmount());
//        this.setVersion(1);
//        this.setCreateTime(new Date());
//    }

    public Long getId() {
        return id;
    }

    public TbUserRedpack setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public TbUserRedpack setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getRedPackId() {
        return redPackId;
    }

    public TbUserRedpack setRedPackId(Long redPackId) {
        this.redPackId = redPackId;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TbUserRedpack setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbUserRedpack setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public TbUserRedpack setVersion(Integer version) {
        this.version = version;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbUserRedpack{" +
        "id=" + id +
        ", userId=" + userId +
        ", redPackId=" + redPackId +
        ", amount=" + amount +
        ", createTime=" + createTime +
        ", version=" + version +
        "}";
    }
}
