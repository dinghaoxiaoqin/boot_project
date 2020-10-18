package com.rrk.oauth.config.auth;

import com.rrk.oauth.config.userConfig.MyUserDetailService;
import com.rrk.oauth.config.userConfig.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Autowired
    MyUserDetailService userDetailsService;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        System.out.println("convertUserAuthentication这里是干是没用的呢------------"+authentication);

        LinkedHashMap response = new LinkedHashMap();
        String name = authentication.getName();
        response.put("username", name);

        Object principal = authentication.getPrincipal();
        System.out.println("获取的参数000000000000"+principal);
//        UserJwt userJwt = null;
//        if(principal instanceof  UserJwt){
//            userJwt = (UserJwt) principal;
//        }else{
            //refresh_token默认不去调用userdetailService获取用户信息，这里我们手动去调用，得到 UserJwt
            UserSecurity userSecurity = userDetailsService.loadUserByUsername(name);
           // userJwt = (UserJwt) userDetails;
        //}
        response.put("name", userSecurity.getUsername());
        response.put("id", userSecurity.getId());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

}
