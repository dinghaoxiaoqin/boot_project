package com.rrk.common.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rrk.common.modules.user.dto.webdto.AddressInfo;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dinghao
 * @since 2020-09-01
 */
@TableName("tb_user_address")
public class TbUserAddress extends Model<TbUserAddress> {

private static final long serialVersionUID=1L;

    /**
     * 地址id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区（县）
     */
    private String district;

    /**
     * 用户详细地址
     */
    private String userAddress;

    /**
     * 是否默认地址（0否，1是）
     */
    private Integer isApprove;

    private Integer isSelect;

    private String userName;

    private String userMobile;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
    public TbUserAddress() {
    }

    public TbUserAddress(Long userId, AddressInfo addressInfo) {
        this.userId = userId;
        this.userMobile = addressInfo.getTel();
        this.userName = addressInfo.getName();
        this.province = addressInfo.getProvince();
        this.city = addressInfo.getCity();
        this.district = addressInfo.getCountry();
        this.userAddress = addressInfo.getAddressDetail();
        if (addressInfo.getIsDefault()) {
            this.isApprove = 1;
        } else {
            this.isApprove = 0;
        }
        this.createTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public TbUserAddress setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public TbUserAddress setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public TbUserAddress setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public TbUserAddress setCity(String city) {
        this.city = city;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public TbUserAddress setDistrict(String district) {
        this.district = district;
        return this;
    }

    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public TbUserAddress setUserAddress(String userAddress) {
        this.userAddress = userAddress;
        return this;
    }

    public Integer getIsApprove() {
        return isApprove;
    }

    public TbUserAddress setIsApprove(Integer isApprove) {
        this.isApprove = isApprove;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TbUserAddress setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TbUserAddress setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbUserAddress{" +
        "id=" + id +
        ", userId=" + userId +
        ", province=" + province +
        ", city=" + city +
        ", district=" + district +
        ", userAddress=" + userAddress +
        ", isApprove=" + isApprove +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", userName=" + userName +
        ", userMobile=" + userMobile +
        "}";
    }
}
