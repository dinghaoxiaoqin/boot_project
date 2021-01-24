package com.rrk.common.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dinghao
 * @since 2020-11-25
 */
@TableName("tb_operator_log")
public class TbOperatorLog extends Model<TbOperatorLog> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderNo;
    /**
     * 操作人
     */
    private String operatorBy;

    /**
     * 操作时间
     */
    private Date createTime;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 操作备注
     */
    private String remarks;


    public Long getId() {
        return id;
    }

    public TbOperatorLog setId(Long id) {
        this.id = id;
        return this;
    }

    public String getOperatorBy() {
        return operatorBy;
    }

    public TbOperatorLog setOperatorBy(String operatorBy) {
        this.operatorBy = operatorBy;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbOperatorLog setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public TbOperatorLog setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    public TbOperatorLog setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public TbOperatorLog setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbOperatorLog{" +
        "id=" + id +
        ", orderNo=" + orderNo +
        ", operatorBy=" + operatorBy +
        ", createTime=" + createTime +
        ", orderStatus=" + orderStatus +
        ", remarks=" + remarks +
        "}";
    }
}
