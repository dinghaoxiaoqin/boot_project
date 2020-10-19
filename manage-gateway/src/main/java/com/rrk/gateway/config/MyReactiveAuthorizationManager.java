package com.rrk.gateway.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.rrk.common.dto.IgnoredUrlsProperties;
import com.rrk.common.modules.manage.dto.EmployeePartDto;
import com.rrk.common.modules.manage.dto.PartPermDto;
import com.rrk.gateway.config.gatewayConfig.gatewayException.MyAccessDeniedException;
import com.rrk.gateway.util.TokenRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限判断（授权管理器）
 */
@Component
public class MyReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private PathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    private TokenRedisUtil tokenRedisUtil;

    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;

    /**
     * 实现权限验证判断
     */
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        //请求资源
        String requestPath = request.getURI().getPath();
        //权限校验
        return authenticationMono.map(auth -> {
            //  System.out.println("auth:"+auth);
            return new AuthorizationDecision(checkAuthorities(auth, request.getMethodValue(), requestPath));
        }).defaultIfEmpty(new AuthorizationDecision(false));
//        return Mono.error(new MyRuntimeException(ResultView.error(ResultEnum.CODE_403)));
    }

    /**
     * 校验是否属于不需要授权的url
     *
     * @param requestPath 请求路径
     * @return
     */
    private boolean permitAll(String requestPath) {

        boolean isFlag = false;
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

    //权限校验
    private boolean checkAuthorities(Authentication auth, String requestMethod, String url) {
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(requestMethod)) {
            return true;
        }
        //1,直接放掉不需要认证授权的url
        if (permitAll(url)) {
            return true;
        }
        // credentials
        Long userId = Convert.toLong(auth.getCredentials());
        //用户拥有的权限
        // Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        List<PartPermDto> partPermDtos = tokenRedisUtil.getPermission();
        //获取系统中角色和权限
        List<EmployeePartDto> partList = tokenRedisUtil.getPartList();
        List<EmployeePartDto> parts = partList.stream().filter(p -> p.getUserId().longValue() == userId.longValue() && p.getIsUse() == 0).collect(Collectors.toList());
        if (CollUtil.isEmpty(parts)) {
            throw new MyAccessDeniedException("用户暂没分配角色，请联系管理员");
        }
        List<Long> longList = parts.stream().map(p -> p.getPartId()).collect(Collectors.toList());
        List<PartPermDto> collect = partPermDtos.stream().filter(p -> p.getPermissionUrl() != null && longList.contains(p.getPartId())).collect(Collectors.toList());
        PartPermDto partPermDto = collect.stream().filter(a -> pathMatcher.match(url, a.getPermissionUrl()) && a.getIsUse() == 0).findFirst().orElse(null);
        if (ObjectUtil.isNull(partPermDto)) {
            throw new MyAccessDeniedException("无权访问,请联系管理员");
        }
        return true;
    }

}
