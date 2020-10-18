package com.rrk.common.constant;

/**
 * 订单类型的枚举
 */
public enum OrderTypeEnum {
    USER_TO_PRODUCT(0,"PO"),
    USER_TO_ORDER(1,"KL"),
    USER_TO_CART(2,"PT");

    private Integer orderType;
    private String  typeValue;
    private OrderTypeEnum(int orderType, String typeValue) {
        this.orderType = orderType;
        this.typeValue = typeValue;
    }

    public static String getTypeValue(int orderType) {
        for (OrderTypeEnum c : OrderTypeEnum.values()) {
            if ( c.getOrderType()== orderType) {
                return c.getTypeValue();
            }
        }
        return null;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }
}
