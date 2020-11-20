package com.rrk.manage.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 对接抖音商品列表实体
 * @author dinghao
 * @date 2020 -11-6
 */
@Data
public class DouyinProductDto implements Serializable {

    /**
     * 主图
     */
    private String img;
    /**
     * 成交价格
     */
    private Double settlement_price;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 商品id
     */
    private String product_id_str;
    /**
     * 折后价
     */
    private Double discount_price;

    private Integer cos_ratio;

    private Integer third_cid;

    private String mobile;

    private String description;

    private String recommend_remark;

    private String check_status;

    private String update_time;
    private String usp;

    private String spec_id;

    private String product_id;

    private String extra;

    private String name;
    private Double market_price;
    private Integer out_product_id;
    private Integer pay_type;
    private Integer second_cid;
    private Integer first_cid;
    private Integer status;
}
