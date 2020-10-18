package com.rrk.common.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.commonDto.PermissionCommonDto;
import com.rrk.common.modules.commonDto.UserCommonDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JwtTokenUtil {

    /**
     * 公钥（rsa非对称加密的公钥）
     */
    public static final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp9qYZN8kwjkVsSbXKq+F5299REivjLREVtN5L74TV19X0JF2/H991TnbfTtZ4CcLgA6KKMmaQI8ixPJRN3nGNQ2xp0R7EDjpDH46OIg/cGSWSL2pFc5twGCPoCDYtQtiLLEnzXrhthpM5NYqk1g0jxbf0wFm4WE3m0SZZPUpIK6l1t25o1lq2hDx8irTKcumKmqmCZEYj5S0pcYnY3oD+hJAm45X/U3lvgZoV7xL9UIiD/98to+78jrgv+9UkryYX97HsZ/c1g5sIp/rPT8B2JkBhDTU0+lejEjo7YwuvUmDZ6/ctJaoNm/mk8N+qtEk28qIGsdIwlFAb5DxRuGOPwIDAQAB-----END PUBLIC KEY-----";

    /**
     * 请求头Authorization
     */
    public static final String REQUEST_HEADER = "Authorization";

    /**
     * 解析jwt明文 或明文中的用户数据
     */
    public static UserCommonDto getUserByToken(String token) {
        UserCommonDto userCommonDto = new UserCommonDto();
        try {
            if (StringUtils.isNotEmpty(token)) {
                Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(PUBLIC_KEY));
                String claims = jwt.getClaims();
                String s = new String(claims.getBytes(), "UTF-8");
                Map<String, Object> map = JSON.parseObject(s, HashMap.class);
                String username = (String) map.get("username");
                userCommonDto.setUserResource(Convert.toInt(map.get("userResource")));
                userCommonDto.setUsername(username);
                Long userId = Convert.toLong(map.get("userId"));
                userCommonDto.setUserId(userId);
                Object authorities = map.get("authorities");
                List<String> list = JSON.parseArray(authorities.toString(), String.class);
                List<PermissionCommonDto> permissionCommonDtos = new ArrayList<>();
                for (String s2 : list) {
                    //  System.out.println("obj:"+object);
                    PermissionCommonDto permissionCommonDto = new PermissionCommonDto();
                    permissionCommonDto.setPermissionUrl(s2);
                    permissionCommonDtos.add(permissionCommonDto);
                }
                userCommonDto.setPermissionCommonDtos(permissionCommonDtos);
                return userCommonDto;
            }
            return null;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取用户id(从请求头中获取)
     */
    public static Long getUserId(HttpServletRequest request) {
        String token = request.getHeader(REQUEST_HEADER);
        try {
            if (StrUtil.isBlank(token)) {
                return null;
            }
            Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(PUBLIC_KEY));
            String claims = jwt.getClaims();
            String s = new String(claims.getBytes(), "UTF-8");
            Map<String, Object> map = JSON.parseObject(s, HashMap.class);
            Long userId = Convert.toLong(map.get("userId"));
            return userId;
        } catch (Exception e) {
            throw new MyException("未登录，请先登录");
        }
    }

    /**
     * 根据token来获取userId
     *
     */
    public static Long getUserIdByToken(String token){
        try {
            if (StrUtil.isBlank(token)) {
                return null;
            }
            Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(PUBLIC_KEY));
            String claims = jwt.getClaims();
            String s = new String(claims.getBytes(), "UTF-8");
            Map<String, Object> map = JSON.parseObject(s, HashMap.class);
            Long userId = Convert.toLong(map.get("userId"));
            return userId;
        } catch (Exception e) {
            throw new MyException("未登录，请先登录");
        }

    }

    /**
     * 判断token是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            if (StrUtil.isNotBlank(token)) {
                Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(PUBLIC_KEY));
                String claims = jwt.getClaims();
                Map<String, Object> map = JSON.parseObject(claims, HashMap.class);
                 Long exp1 = Convert.toLong(map.get("exp"))*1000;
                if (exp1 > System.currentTimeMillis()) {
                    return true;
                }
                return false;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }


}
