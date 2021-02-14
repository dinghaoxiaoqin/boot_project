package com.rrk.order.manage.config.aopConfig;

import com.rrk.order.manage.config.dynamicDataSource.DataSource;
import com.rrk.order.manage.config.dynamicDataSource.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 自定义多数据源切换的切面
 */
@Aspect
@Component
@Slf4j
@Order(-100)
public class DataSourceAspect {

    /**
     * 确定数据源的切点
     */
//    @Pointcut("@annotation(com.rrk.dataSources.DataSourceAnnonation)")
//    public void dataSourcePointCut() {
//    }

    @Pointcut("@within(com.rrk.order.manage.config.dynamicDataSource.DataSource) || @annotation(com.rrk.order.manage.config.dynamicDataSource.DataSource)")
    public void pointcut() {
    }




    @Before("pointcut() && @annotation(dataSource)")
    public void doBefore(DataSource dataSource) {
        log.info("选择的数据源是：{}",dataSource.value().getValue());
        DataSourceContextHolder.setDataSource(dataSource.value().getValue());
    }

    @After("pointcut()")
    public void jarvisWxDb () {
        log.info("清除上下文数据");
        DataSourceContextHolder.clear();
    }
}
