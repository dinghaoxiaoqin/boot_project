package com.rrk.oauth.config.auth;

import cn.hutool.core.date.DateUtil;
import com.rrk.common.modules.manage.entity.TbEmployee;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义token生成携带的信息(扩展token所携带的信息)
 */
@Component
public class MyTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        Object principal = authentication.getPrincipal();
        TbEmployee employee = (TbEmployee) principal;
        additionalInfo.put("userId", employee.getEmployeeId());
        additionalInfo.put("username", employee.getUsername());
        additionalInfo.put("createTime", DateUtil.formatDate(new Date()));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}
