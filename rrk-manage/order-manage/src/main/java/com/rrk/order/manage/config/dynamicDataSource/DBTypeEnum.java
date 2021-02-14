package com.rrk.order.manage.config.dynamicDataSource;

public enum DBTypeEnum {

    boot("boot"),
    bootOrder("bootorder");
    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
