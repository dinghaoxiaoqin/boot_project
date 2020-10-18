package com.rrk.common.modules.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserMenuDto implements Serializable {

    /**
     * 权限的url
     */
    private String menuUrl;
    /**
     * 权限唯一标识
     */
    private String menuSign;

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuSign() {
        return menuSign;
    }

    public void setMenuSign(String menuSign) {
        this.menuSign = menuSign;
    }
}
