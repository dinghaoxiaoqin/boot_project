//package com.rrk.common.modules.order.dto;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.rrk.common.modules.order.entity.TbGeneralOrder;
//import com.rrk.common.modules.order.entity.TbOrder;
//import com.rrk.common.modules.product.entity.TbProduct;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// * pc端订单详情
// */
//@Getter
//@Setter
//@Data
//public class OrderDetailDto implements Serializable {
//
//    /**
//     * 订单号
//     */
//    private String orderId;
//
//    /**
//     * 下单时间
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date createTime;
//
//    /**
//     * 商品图片
//     */
//    private String productUrl;
//
//    /**
//     * 用户id
//     */
//    private Long userId;
//
//    /**
//     * 商品名称
//     */
//    private String productName;
//
//    /**
//     * 购买数量
//     */
//    private Integer buyNum;
//
//    /**
//     * 订单状态（0未支付，1 已支付）
//     */
//    private Integer orderStatus;
//
//    /**
//     * 订单金额
//     */
//    private BigDecimal orderAmount;
//
//    /**
//     * 商品售价
//     */
//    private BigDecimal productPrice;
//
//    /**
//     * 商品编号
//     */
//    private Long productId;
//
//    public OrderDetailDto(){
//
//    }
//    public OrderDetailDto(TbProduct product, TbOrder tbOrder) {
//        this.setBuyNum(tbOrder.getProductNum());
//        this.setCreateTime(tbOrder.getCreateTime());
//        this.setOrderAmount(tbOrder.getOrderAmount());
//        this.setOrderId(tbOrder.getOrderId());
//        this.setOrderStatus(tbOrder.getOrderStatus());
//        this.setUserId(tbOrder.getUserId());
//        this.setProductId(product.getId());
//        if (ObjectUtil.isNotNull(product)) {
//            this.setProductName(product.getProductName());
//            this.setProductUrl(product.getProductUrl());
//            this.setProductPrice(product.getProductPrice());
//        }
//    }
//
//    public OrderDetailDto(TbProduct product, TbGeneralOrder generalOrder) {
//        this.setBuyNum(generalOrder.getBuyNum());
//        this.setCreateTime(generalOrder.getCreateTime());
//        this.setOrderAmount(generalOrder.getOrderAmount());
//        this.setOrderId(generalOrder.getOrderId());
//        this.setOrderStatus(generalOrder.getOrderStatus());
//        this.setUserId(generalOrder.getUserId());
//        this.setProductId(product.getId());
//        if (ObjectUtil.isNotNull(product)) {
//            this.setProductName(product.getProductName());
//            this.setProductUrl(product.getProductUrl());
//            this.setProductPrice(product.getProductPrice());
//        }
//    }
//}
