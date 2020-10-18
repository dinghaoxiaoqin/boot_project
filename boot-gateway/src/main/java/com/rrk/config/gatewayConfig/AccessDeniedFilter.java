package com.rrk.config.gatewayConfig;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.constant.NumConstants;
import com.rrk.common.dto.IgnoredUrlsProperties;
import com.rrk.common.modules.user.dao.TbUserMapper;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.common.utils.JwtTokenUtil;
import com.rrk.utils.TokenRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 授权过滤器
 */
@Component
@Slf4j
public class AccessDeniedFilter implements GlobalFilter, Ordered {

    @Autowired
    private TokenRedisUtil tokenRedisUtil;
    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;

//    @Autowired
//    private JsonExceptionHandle jsonExceptionHandler;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    public static final String REQUEST_HEADER = "Authorization";

    /**
     * 对token认证通过的接口进行授权
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            boolean isFlag = isFlag(request);
            if (isFlag) {
                return chain.filter(exchange);
            }
            //todo:判断用户是否有权限访问某些商品 或接口（验证用户的会员等级）
            String token = request.getHeaders().getFirst(REQUEST_HEADER);
            Long userId = JwtTokenUtil.getUserIdByToken(token);
            TbUser user = userMapper.selectOne(new QueryWrapper<TbUser>().eq("user_id", userId));
           // todo: 与redis 的key-value (key 已访问路径的字符串，value就是会员等级) user.getUserRank() 判断是否有权限访问
            if (user.getUserRank() < 1) {
                throw new MyAccessDeniedException("请先升级为会员");
            }

        } catch (Exception e) {
           log.error("网关授权出现异常：e->{}",e);
        }
        //授权后就放行
        return chain.filter(exchange);
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
                //不需要授权 直接通过
                isFlag = true;
                break;
            }
        }
        return isFlag;
    }

    /**
     * 过滤器的执行顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return NumConstants.GATEWAY_SORT_SECOND;
    }
}
