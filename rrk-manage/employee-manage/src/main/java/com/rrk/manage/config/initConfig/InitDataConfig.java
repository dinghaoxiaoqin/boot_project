package com.rrk.manage.config.initConfig;

import com.rrk.manage.service.InitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动时，就将角色和权限信息初始化到缓存
 * @author dinghao
 */
@Component
@Slf4j
public class InitDataConfig implements ApplicationRunner {

    @Autowired
    private InitService initService;

    /**
     * 项目启动就初始化
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("角色权限初始化开始了----------------");
        initService.initPartAndPermission();
    }
}
