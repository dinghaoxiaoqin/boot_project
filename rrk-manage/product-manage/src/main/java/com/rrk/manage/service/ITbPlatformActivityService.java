package com.rrk.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.entity.TbPlatformActivity;
import com.rrk.manage.dto.PlatformDto;

import java.text.ParseException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */

public interface ITbPlatformActivityService extends IService<TbPlatformActivity> {

    Boolean editActivity(PlatformDto platformDto) throws ParseException;

    IPage<TbPlatformActivity> getActivityList(Integer pageNo, Integer pageSize, String keyword);

    TbPlatformActivity getActivityDetail(Integer id);
}
