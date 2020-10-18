package com.rrk.common.constant;

/**
 * 用户行为的枚举
 */
public enum UserActionEnum {

    USER_TO_PRODUCT(0,"用户浏览商品详情"),
    USER_TO_ORDER(1,"用户下单"),
    USER_TO_CART(2,"商品加入购物车");

    private int actionType;
    private String actionDesc;

    private UserActionEnum(int actionType, String actionDesc) {
        this.actionType = actionType;
        this.actionDesc = actionDesc;
    }


    public static String getMessage(int actionType) {
        for (UserActionEnum c : UserActionEnum.values()) {
            if (c.getActionType() == actionType) {
                return c.actionDesc;
            }
        }
        return null;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }
}
