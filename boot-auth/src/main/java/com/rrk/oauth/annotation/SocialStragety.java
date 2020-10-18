package com.rrk.oauth.annotation;

import cn.hutool.core.util.ObjectUtil;
import com.rrk.common.handle.MyException;
import com.rrk.oauth.service.SocialHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理社交登录的策略（采用设计模式：策略模式,单例模式）
 */
@Component
@Slf4j
public class SocialStragety implements ApplicationContextAware, CommandLineRunner {

    /**
     * 1把创建spring 的上下文对象改装成一个绝对安全的单例（主要利用volatile 来禁止指令重排）
     */
    private volatile ApplicationContext applicationContext;
    /**
     * 2,初始化一个map(key社交登录的类型值，value是与之对应社交业务处理的实现类)
     */
    private static Map<Integer, SocialHandleService> map;

    /**
     * 设置spring 的上下文对象
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 3，类初始化时，将注解类型对应社交登录列表保存到map
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        initSocialMap();
    }

    private void initSocialMap() {
        //1，社交登录类型列表
        Collection<SocialHandleService> values = applicationContext.getBeansOfType(SocialHandleService.class).values();
        map = new HashMap<>(values.size());
        for (SocialHandleService value : values) {
            //2,获取该类的字节码类型
            Class<? extends SocialHandleService> aClass = value.getClass();
            //3,根据字节码对象获取注解的对象
            SocialType annotation = aClass.getAnnotation(SocialType.class);
            if (ObjectUtil.isNotNull(annotation)) {
                map.put(annotation.socialType(), value);
            }
            log.info("初始化社交登录的类型列表：map->{}", map);
        }
    }

    /**
     * 4，单例要给外面的业务进行调用(根据传入的社交登录类型获取对应的登录处理业务实现类)
     */
    public static SocialHandleService getSocialHandleService(Integer socialType) {
        SocialHandleService socialHandleService = map.get(socialType);
        if (ObjectUtil.isNotNull(socialHandleService)) {
            return socialHandleService;
        }
        throw new MyException("暂不存在该类型的社交登录方式");
    }
}
