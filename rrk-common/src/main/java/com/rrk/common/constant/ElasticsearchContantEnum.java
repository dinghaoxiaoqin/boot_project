package com.rrk.common.constant;

/**
 * elasticsearch相关常量的枚举
 */
public enum ElasticsearchContantEnum {

    LUNBO_PRODUCT_COUNT(5, "轮播商品数量"),
    RECOMMEND_PRODUCT_COUNT(4, "精品推荐商品数"),
    HOT_PRODUCT_COUNT(6, "热搜展示商品数"),
    SUGGESTERS_COUNT(10,"建议搜索展示的商品数")
    ;

    private int code;
    private String descrition;

    private ElasticsearchContantEnum(int code, String descrition) {
        this.code = code;
        this.descrition = descrition;
    }

    public static String getMessage(int code) {
        for (ElasticsearchContantEnum c : ElasticsearchContantEnum.values()) {
            if (c.getCode() == code) {
                return c.descrition;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }
}
