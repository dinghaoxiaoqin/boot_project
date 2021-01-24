package com.rrk.common.handle;

/**
 * 多数据源的枚举
 * @author dh
 * @date 2020-11-13
 */
public enum  DatasourceEnum {

    BOOTORDER("bootorder"),
    BOOT("boot");

    private String value;



    DatasourceEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
