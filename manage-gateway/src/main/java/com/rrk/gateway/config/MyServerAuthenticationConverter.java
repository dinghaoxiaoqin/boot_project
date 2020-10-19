package com.rrk.gateway.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.rrk.common.dto.IgnoredUrlsProperties;
import com.rrk.common.modules.commonDto.PermissionCommonDto;
import com.rrk.common.modules.commonDto.UserCommonDto;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.gateway.config.gatewayConfig.gatewayException.MyAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerFormLoginAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dinghao
 * @date 2020-6-28
 */
@Component
@Slf4j
public class MyServerAuthenticationConverter extends ServerFormLoginAuthenticationConverter {

    public static final String REQUEST_HEADER = "Authorization";

    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;

    /**
     * 用户的请求首先进到这里来
     *
     * @param exchange
     * @return
     */
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
       // log.info("获取用户请求:exchange->{}" + exchange);
        ServerHttpRequest request = exchange.getRequest();
        //从请求头中获取用户的token
        String token = request.getHeaders().getFirst(REQUEST_HEADER);
      //  log.info("用户token:token->{}", token);
        //获取请求资源路径
        if (StrUtil.isBlank(token)) {
            boolean isFlag = isFlag(request);
            //不需要token认证的接口，就直接放行
            if (!isFlag) {
                throw new MyAuthenticationException("未登录，请先登录");
            }
        }
        if (StringUtils.isNotBlank(token)) {
            //校验令牌的有效(过期)
            boolean flag = JwtTokenUtil.isTokenExpired(token);
            if (!flag) {
                throw new MyAuthenticationException("账号已过期，请重新登录！");
            }
        }
        //从token里面获取用户的权限信息
        UserCommonDto userCommonDto = JwtTokenUtil.getUserByToken(token);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if (ObjectUtil.isNotNull(userCommonDto)) {
            if (CollUtil.isNotEmpty(userCommonDto.getPermissionCommonDtos())) {
                for (PermissionCommonDto permissionCommonDto : userCommonDto.getPermissionCommonDtos()) {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permissionCommonDto.getPermissionUrl());
                    authorityList.add(simpleGrantedAuthority);
                }
            }
        } else {
            userCommonDto =  new UserCommonDto();
            userCommonDto.setUserId(-100L);
        }
        MyAccountAuthentication myAccountAuthentication = new MyAccountAuthentication( userCommonDto.getUserId(), request.getPath().toString(), authorityList);
        return Mono.just(myAccountAuthentication);
    }

    private boolean isFlag(ServerHttpRequest request) {
        boolean isFlag = false;
        String requestPath = request.getPath().toString();
        if (requestPath.contains("?")) {
            int i = requestPath.indexOf("?");
            requestPath = requestPath.substring(0, i);
        }
        for (String ignoreUrl : ignoredUrlsProperties.getUrls()) {
            if (ignoreUrl.equals(requestPath)) {
                //不需要认证 直接通过
                isFlag = true;
                break;
            }
        }
        return isFlag;
    }
}
