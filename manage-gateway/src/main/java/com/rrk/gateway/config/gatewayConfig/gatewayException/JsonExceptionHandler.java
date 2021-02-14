//package com.rrk.gateway.config.gatewayConfig.gatewayException;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
//import org.springframework.cloud.gateway.support.NotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.codec.HttpMessageReader;
//import org.springframework.http.codec.HttpMessageWriter;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import org.springframework.web.reactive.result.view.ViewResolver;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 异常监听器
// */
//@Component
//@Slf4j
//public class JsonExceptionHandler implements ErrorWebExceptionHandler {
//
//
////    @Override
////    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
////         R r = new R<>();
////        if (ex instanceof MyAuthenticationException) {
////            r = R.fail(436,((MyAuthenticationException) ex).getMsg());
////        } else if(ex instanceof MyAccessDeniedException){
////             r = R.fail(437,((MyAccessDeniedException) ex).getMsg());
////        } else {
////            r = R.fail(500,"未知异常，请联系管理员");
////        }
////      //  exchange.getResponse().setStatusCode();
////        exchange.getResponse().getHeaders().add("Content-Type","application/json;charset=UTF-8");
////        byte[] bytes = JSON.toJSONString(r).getBytes(StandardCharsets.UTF_8);
////        DataBuffer wrap = exchange.getResponse().bufferFactory().wrap(bytes);
////        return exchange.getResponse().writeWith(Flux.just(wrap));
////    }
//
//    /**
//     * MessageReader
//     */
//    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
//
//    /**
//     * MessageWriter
//     */
//    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
//
//    /**
//     * ViewResolvers
//     */
//    private List<ViewResolver> viewResolvers = Collections.emptyList();
//
//    /**
//     * 存储处理异常后的信息
//     */
//    private ThreadLocal<Map<String, Object>> exceptionHandlerResult = new ThreadLocal<>();
//
//
//    /**
//     * 参考AbstractErrorWebExceptionHandler
//     */
//    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
//        Assert.notNull(messageReaders, "'messageReaders' must not be null");
//        this.messageReaders = messageReaders;
//    }
//
//    /**
//     * 参考AbstractErrorWebExceptionHandler
//     */
//    public void setViewResolvers(List<ViewResolver> viewResolvers) {
//        this.viewResolvers = viewResolvers;
//    }
//
//    /**
//     * 参考AbstractErrorWebExceptionHandler
//     */
//    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
//        Assert.notNull(messageWriters, "'messageWriters' must not be null");
//        this.messageWriters = messageWriters;
//    }
//
//
//    @Override
//    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
//        // 按照异常类型进行处理
//        HttpStatus httpStatus;
//        String message;
//        if (ex instanceof NotFoundException) {
//            httpStatus = HttpStatus.NOT_FOUND;
//            message = "Service Not Found";
//        } else if (ex instanceof MyAuthenticationException) {
//            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
//            httpStatus = responseStatusException.getStatus();
//            message = responseStatusException.getReason();
//        } else if (ex instanceof MyAccessDeniedException) {
//            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
//            httpStatus = responseStatusException.getStatus();
//            message = responseStatusException.getReason();
//        }
//
//        else if (ex instanceof ResponseStatusException) {
//            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
//            httpStatus = responseStatusException.getStatus();
//            message = responseStatusException.getMessage();
//        } else {
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//            message = StringUtils.isEmpty(ex.getMessage()) ? "Internal Server Error" : ex.getMessage();
//        }
//
//        //封装响应体,此body可修改为自己的jsonBody
//        Map<String, Object> result = new HashMap<>(2, 1);
//        result.put("httpStatus", httpStatus);
//        if (httpStatus.value() == 200) {
//            String msg = "{\"code\":" + 436+ ",\"msg\": \"" + message + "\"}";
//            result.put("body", msg);
//        }else {
//            String msg = "{\"code\":" + httpStatus.value()+ ",\"msg\": \"" + message + "\"}";
//            result.put("body", msg);
//        }
//        if (exchange.getResponse().isCommitted()) {
//            return Mono.error(ex);
//        }
//        exceptionHandlerResult.set(result);
//        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
//        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
//                .switchIfEmpty(Mono.error(ex))
//                .flatMap((handler) -> handler.handle(newRequest))
//                .flatMap((response) -> write(exchange, response));
//
//
//    }
//
//    /**
//     * 参考DefaultErrorWebExceptionHandler
//     */
//    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
//        Map<String, Object> result = exceptionHandlerResult.get();
//        return ServerResponse.status((HttpStatus) result.get("httpStatus"))
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(BodyInserters.fromObject(result.get("body")));
//    }
//
//    /**
//     * 参考AbstractErrorWebExceptionHandler
//     */
//    private Mono<? extends Void> write(ServerWebExchange exchange,
//                                       ServerResponse response) {
//        exchange.getResponse().getHeaders()
//                .setContentType(response.headers().getContentType());
//        return response.writeTo(exchange, new ResponseContext());
//    }
//
//    /**
//     * 参考AbstractErrorWebExceptionHandler
//     */
//    private class ResponseContext implements ServerResponse.Context {
//
//        @Override
//        public List<HttpMessageWriter<?>> messageWriters() {
//            return JsonExceptionHandler.this.messageWriters;
//        }
//
//        @Override
//        public List<ViewResolver> viewResolvers() {
//            return JsonExceptionHandler.this.viewResolvers;
//        }
//
//    }
//
//
//}
