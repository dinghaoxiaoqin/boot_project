//package com.rrk.gateway.config.gatewayConfig;
//
//import cn.hutool.core.util.StrUtil;
//import com.rrk.common.constant.NumConstants;
//import com.rrk.common.dto.IgnoredUrlsProperties;
//import com.rrk.common.utils.JwtUtil;
//import com.rrk.gateway.config.gatewayConfig.gatewayException.MyAuthenticationException;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * @author dinghao
// * 通过gateway的全局过滤器对用户访问token进行认证
// */
//@Component
//@Slf4j
//public class AuthorizeFilter implements GlobalFilter, Ordered {
//
//    public static final String REQUEST_HEADER = "Authorization";
//
//    @Autowired
//    private IgnoredUrlsProperties ignoredUrlsProperties;
//
//    /**
//     * 全局拦截
//     * 用户认证
//     *
//     * @param exchange
//     * @param chain
//     * @return
//     */
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//            //获取token 的方式 1 请求头 2参数获取 3cookie中
//            String token = request.getHeaders().getFirst(REQUEST_HEADER);
//            log.info("用户token:token->{}",token);
//            if (StrUtil.isBlank(token)) {
//                //参数获取
//                token = request.getQueryParams().getFirst(REQUEST_HEADER);
//            }
//            //没有token就进行拦截
//            if (StrUtil.isBlank(token)) {
//                boolean isFlag = isFlag(request);
//                //不需要token认证的接口，就直接放行
//                if (!isFlag) {
//                    throw new MyAuthenticationException("未登录，请先登录");
//                }
//            }
//            if (StringUtils.isNotBlank(token)) {
//                //校验令牌的有效(过期)
//                boolean flag = JwtUtil.isTokenExpired(token);
//                if (!flag) {
//                    throw new MyAuthenticationException("账号已过期，请重新登录！");
//                }
//            }
//        return chain.filter(exchange);
//    }
//
//    private boolean isFlag(ServerHttpRequest request) {
//        boolean isFlag = false;
//        String requestPath = request.getPath().toString();
//        if (requestPath.contains("?")) {
//            int i = requestPath.indexOf("?");
//            requestPath = requestPath.substring(0, i);
//        }
//        for (String ignoreUrl : ignoredUrlsProperties.getUrls()) {
//            if (ignoreUrl.equals(requestPath)) {
//                //不需要认证 直接通过
//                isFlag = true;
//                break;
//            }
//        }
//        return isFlag;
//    }
//
//    /**
//     * 这个是过滤器排序（越小就越先执行）
//     *
//     * @return
//     */
//    @Override
//    public int getOrder() {
//        return NumConstants.GATEWAY_SORT_FIRST;
//    }
//}
