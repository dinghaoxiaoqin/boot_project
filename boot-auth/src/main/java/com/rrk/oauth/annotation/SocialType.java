package com.rrk.oauth.annotation;

import java.lang.annotation.*;

/**
 * @date 2020-8-8
 * @author dinghao
 * 自定义社交登录分类的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SocialType {

    int socialType() default 0;
}
