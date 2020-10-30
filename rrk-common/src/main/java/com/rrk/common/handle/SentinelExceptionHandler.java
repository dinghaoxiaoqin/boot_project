//package com.rrk.common.handle;
//
//import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
//import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
//import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
//import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
//import com.google.gson.Gson;
//import com.rrk.common.R;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @author dinghao
// * @date 2020-10-06
// * 定义全局的sentinel的异常处理
// * 主要包括： （1）限流异常处理 （2）熔断异常处理 (3)系统保护 (4)授权
// * 注意： 一定使用public 不能是private
// */
//@Slf4j
//@Component
//public class SentinelExceptionHandler implements UrlBlockHandler {
//
//    /**
//     * 自定义异常返回
//     * @param request
//     * @param response
//     * @param e
//     * @throws IOException
//     */
//    @Override
//    public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
//        R<Object> r = new R<>();
//        if (e instanceof FlowException) {
//            //限流
//            r.setMsg("系统繁忙，稍后再试........");
//            r.setCode(201);
//        } else if (e instanceof DegradeException) {
//            //服务降级
//            r.setMsg("网络异常.............");
//            r.setCode(202);
//        } else if (e instanceof ParamFlowException) {
//            //限流
//            r.setMsg("系统繁忙，稍后再试.......");
//            r.setCode(203);
//        } else if (e instanceof SystemBlockException) {
//            //系统保护
//            r.setMsg("系统维护中.......");
//            r.setCode(204);
//        } else if (e instanceof AuthorityException) {
//            //授权
//            r.setMsg("系统繁忙，稍后再试.......");
//            r.setCode(205);
//        }
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-Type","application/json;charset=UTF-8");
//        response.getWriter().print(new Gson().toJson(r));
//    }
//}
