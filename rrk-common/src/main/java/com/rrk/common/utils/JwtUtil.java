package com.rrk.common.utils;

import cn.hutool.core.convert.Convert;
import com.rrk.common.modules.commonDto.PermissionCommonDto;
import com.rrk.common.modules.commonDto.RoleCommonDto;
import com.rrk.common.modules.commonDto.UserCommonDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Classname JwtUtil
 * @Description JWT工具类
 * @Author  dinghao
 * @Date 2020-1-20
 */
@Log4j2
@Component
public class JwtUtil {

    /**
     * 用户名称
     */
    private static final String USERNAME = "username";

    private static final String EMPLOYEEID = "userId";
    /**
     * 创建时间
     */
    private static final String CREATED = "createTime";
    /**
     * 权限列表
     */
    private static final String AUTHORITIES = "authorities";
    /**
     * 密钥
     */
    private static final String SECRET = "dinghao";
    /**
     * 有效期1小时
     */
    private static final long EXPIRE_TIME = 60 * 60 * 1000;

    /**
     * 请求头Authorization
     */
    public static final String REQUEST_HEADER = "Authorization";
    /**
     * 角色
     */
    public static final String ROLE_DATA = "roles";

    /**
     * 用户来源(0 后台 ，1前台用户)
     */
    private static final String USER_RESOURCE = "userResource";

    /**
     * 生成令牌
     *
     * @return 令牌
     */
    public static String generateToken(UserCommonDto userCommonDto) {
        Map<String, Object> claims = new HashMap<>(6);
        String username = userCommonDto.getUsername();
        claims.put(EMPLOYEEID, userCommonDto.getUserId());
        claims.put(USERNAME, username);
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, userCommonDto.getPermissionCommonDtos());
        claims.put(ROLE_DATA, userCommonDto.getRoleCommonDtos());
        claims.put(USER_RESOURCE, userCommonDto.getUserResource());
        return generateToken(claims);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Convert.toStr(claims.get(USERNAME));
    }

    /**
     * x`
     * 获取userId
     */
    public static Long getUserId(HttpServletRequest request) {
        String token = request.getHeader(REQUEST_HEADER);
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = getClaimsFromToken(token);
            Long employeeId = Convert.toLong(claims.get(EMPLOYEEID));
            return employeeId;
        }
        return null;
    }

    /**
     * 根据请求令牌获取登录认证信息
     *
     * @return 用户名
     */
    public static  UserCommonDto getUserFromToken(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return null;
            }
            String username = (String) claims.get(USERNAME);
            if (username == null) {
                return null;
            }
            if (isTokenExpired(token)) {
                return null;
            }
            // 解析对应的角色和权限以及用户信息
            Object permissionObj = claims.get(AUTHORITIES);
            Object roleObj = claims.get(ROLE_DATA);
            Integer userId = (Integer) claims.get(EMPLOYEEID);
           // Set<String> perms = new HashSet<>();
            List<PermissionCommonDto> permissionCommonDtos = new ArrayList<>();
            if (permissionObj instanceof ArrayList<?>) {
                for (Object object : (List<?>) permissionObj) {
                    permissionCommonDtos.add(PermissionCommonDto.class.cast(object));
                }
            }
            List<RoleCommonDto> roleCommonDtoList = new ArrayList<>();
            if (roleObj instanceof ArrayList<?>) {
                for (Object o : (List<?>) roleObj) {
                    roleCommonDtoList.add(RoleCommonDto.class.cast(o));
                }
            }
             UserCommonDto userCommonDto = new UserCommonDto();
            if (validateToken(token, username)) {
                userCommonDto.setUserId(Convert.toLong(userId));
                userCommonDto.setUsername(username);
                userCommonDto.setPermissionCommonDtos(permissionCommonDtos);
                userCommonDto.setRoleCommonDtos(roleCommonDtoList);
                userCommonDto.setUserResource((Integer) claims.get(USER_RESOURCE));
                return userCommonDto;
            }
        }
        return null;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 验证令牌
     *
     * @param token
     * @param username
     * @return
     */
    public static Boolean validateToken(String token, String username) {
        String userName = getUsernameFromToken(token);
        return (userName.equals(username) && !isTokenExpired(token));
    }

    /**
     * 刷新令牌
     *
     * @param token
     * @return
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return
     */
    private static  String getToken(HttpServletRequest request) {
        String token = request.getHeader(REQUEST_HEADER);
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        return null;
    }

}
