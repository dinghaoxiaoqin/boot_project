package com.rrk.common.handle;

import com.rrk.common.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常捕获类
 */
@RestControllerAdvice
public class MyExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public R bootErrorHandler(HttpServletRequest request, Throwable e) {
        logger.info("捕获到的异常" + e);
        R r = new R();
        MyException be = (MyException) e;
        String errMsg = e.getMessage();
        if (errMsg == null) {
            r.setCode(CustomExceptionEnum.USER_UNAUTHORIZED.getCode());
            r.setMsg(CustomExceptionEnum.USER_UNAUTHORIZED.getMessage());
        } else {
            r.setCode(be.getCode());
            r.setMsg(be.getMessage());
        }
        return r;

    }

    /**
     * 500的异常捕捉处理
     */

    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, Exception e) {
        logger.info("捕获到的异常" + e);
        //logger.error("未知异常！原因是:",e);
        R<Object> r = new R<>();
        r.setCode(CustomExceptionEnum.INTERNAL_SERVER_ERROR.getCode());
        r.setMsg(CustomExceptionEnum.INTERNAL_SERVER_ERROR.message);
        return r;
    }

    /**
     * 登录异常处理
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public R loginExceptionHandler(HttpServletRequest req,Exception e){
        R<Object> r = new R<>();
        LoginException be = (LoginException) e;
        String errMsg = e.getMessage();
        r.setCode(be.getCode());
        r.setMsg(be.getMsg());
        return r;
    }

    /**
     * 订单异常处理
     */
    @ExceptionHandler(value = OrderException.class)
    @ResponseBody
    public R orderExceptionHandler(HttpServletRequest request,Exception e){
        R<Object> r = new R<>();
        LoginException be = (LoginException) e;
        String errMsg = e.getMessage();
        r.setCode(be.getCode());
        r.setMsg(be.getMsg());
        return r;
    }
}
