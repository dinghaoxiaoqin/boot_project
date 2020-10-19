//package com.rrk.gateway.config.gatewayConfig;
//
//import cn.hutool.core.collection.CollUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.rrk.common.constant.NumConstants;
//import com.rrk.common.dto.IgnoredUrlsProperties;
//import com.rrk.common.modules.user.dao.TbMenuMapper;
//import com.rrk.common.modules.user.dao.TbRoleMapper;
//import com.rrk.common.modules.user.dao.TbRoleMenuMapper;
//import com.rrk.common.modules.user.entity.TbMenu;
//import com.rrk.common.modules.user.entity.TbRole;
//import com.rrk.common.modules.user.entity.TbRoleMenu;
//import com.rrk.gateway.config.gatewayConfig.gatewayException.MyAccessDeniedException;
//import com.rrk.gateway.util.TokenRedisUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * 授权过滤器
// */
//@Component
//@Slf4j
//public class AccessDeniedFilter implements GlobalFilter, Ordered {
//
//    @Autowired
//    private TokenRedisUtil tokenRedisUtil;
//    @Autowired
//    private TbMenuMapper menuMapper;
//
//    @Autowired
//    private TbRoleMenuMapper roleMenuMapper;
//
//    @Autowired
//    private TbRoleMapper roleMapper;
//
//    @Autowired
//    private IgnoredUrlsProperties ignoredUrlsProperties;
//
////    @Autowired
////    private JsonExceptionHandle jsonExceptionHandler;
//
//    private AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//
//    /**
//     * 对token认证通过的接口进行授权
//     * @param exchange
//     * @param chain
//     * @return
//     */
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        try {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//            boolean isFlag = isFlag(request);
//            if (isFlag) {
//                return chain.filter(exchange);
//            }
//            List<String> roleCodeList = new ArrayList<>();
//            //获取访问当前请求url对应的权限
//            List<TbMenu> menuList = tokenRedisUtil.getMenuList();
//            if (CollUtil.isEmpty(menuList)) {
//                menuList = menuMapper.selectList(null);
//            }
//            if (CollUtil.isNotEmpty(menuList)) {
//                tokenRedisUtil.addMenuList(menuList);
//            }
//            if (CollUtil.isEmpty(menuList)) {
//                // 如果数据中没有找到相应url资源则为非法访问，要求用户登录再进行操作
//               // myAccessDeniedExceptionHandler.myAccessDeniedException(response,"无权访问");
//                throw new MyAccessDeniedException("无权访问");
//            }
//            List<Integer> menuIds = menuList.stream().filter(m -> antPathMatcher.match(request.getPath().toString(), m.getUrl())).map(m -> m.getId()).collect(Collectors.toList());
//            if (CollUtil.isEmpty(menuIds)) {
//              //  myAccessDeniedExceptionHandler.myAccessDeniedException(response,"无权限访问");
//                throw new MyAccessDeniedException("无权限访问");
//            }
//            List<TbRoleMenu> roleMenuList = roleMenuMapper.selectList(new QueryWrapper<TbRoleMenu>().in("menu_id", menuIds));
//            if (CollUtil.isNotEmpty(roleMenuList)) {
//                List<TbRole> roleList = roleMapper.selectList(new QueryWrapper<TbRole>().in("id", roleMenuList.stream().map(r -> r.getRoleId()).collect(Collectors.toSet())));
//                if (CollUtil.isEmpty(roleList)) {
//                   // myAccessDeniedExceptionHandler.myAccessDeniedException(response,"请先升级为会员");
//                    throw new MyAccessDeniedException("请先升级为会员");
//                }
//            } else {
//              //  myAccessDeniedExceptionHandler.myAccessDeniedException(response,"没有该权限的角色");
//                throw new MyAccessDeniedException("没有该权限的角色");
//            }
//        } catch (Exception e){
//           // jsonExceptionHandler.handle(exchange,e);
//        }
//        //授权后就放行
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
//                //不需要授权 直接通过
//                isFlag = true;
//                break;
//            }
//        }
//        return isFlag;
//    }
//    /**
//     * 过滤器的执行顺序
//     * @return
//     */
//    @Override
//    public int getOrder() {
//        return NumConstants.GATEWAY_SORT_SECOND;
//    }
//}
