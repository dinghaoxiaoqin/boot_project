//package com.rrk.common.modules.order.dto;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.rrk.common.modules.product.entity.TbProduct;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//
///**
// * pc端订单列表
// */
//@Data
//@Getter
//@Setter
//public class OrderListDto implements Serializable {
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
//     * 商品编号
//     */
//    private Long productId;
//
//    public OrderListDto(){}
//
//    public OrderListDto(OrderListDto listDto, List<TbProduct> productList) {
//        this.setBuyNum(listDto.getBuyNum());
//        this.setCreateTime(listDto.getCreateTime());
//        this.setOrderAmount(listDto.getOrderAmount());
//        this.setOrderId(listDto.getOrderId());
//        this.setOrderStatus(listDto.getOrderStatus());
//        this.setUserId(listDto.getUserId());
//        this.setProductId(listDto.getProductId());
//         TbProduct product = productList.stream().filter(p -> listDto.getProductId().longValue() == p.getId().longValue()).findFirst().orElse(null);
//        if (ObjectUtil.isNotNull(product)) {
//            this.setProductName(product.getProductName());
//            this.setProductUrl(product.getProductUrl());
//        }
//    }
//}
