package com.rrk.manage.service;

public interface InitService {

    /**
     * 项目启动就将角色和权限信息初始化到 redis
     */
    void initPartAndPermission();
}
