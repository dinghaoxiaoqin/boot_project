package com.rrk.manage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.product.dao.TbPlatformActivityMapper;
import com.rrk.common.modules.product.entity.TbPlatformActivity;
import com.rrk.common.utils.DateUtils;
import com.rrk.manage.dto.PlatformDto;
import com.rrk.manage.service.ITbPlatformActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@Service
@Slf4j
@Transactional(rollbackFor = MyException.class)
public class TbPlatformActivityServiceImpl extends ServiceImpl<TbPlatformActivityMapper, TbPlatformActivity> implements ITbPlatformActivityService {


    @Autowired
    private ITbPlatformActivityService platformActivityService;

    @Autowired
    private TbPlatformActivityMapper platformActivityMapper;

    /**
     * 编辑活动
     * @param platformDto
     * @return
     */
    @Override
    public Boolean editActivity(PlatformDto platformDto) throws ParseException {
        log.info("编辑活动前端传来的参数：platformDto->{}",platformDto);
        TbPlatformActivity platformActivity;
        //判断是添加还是修改
        TbPlatformActivity activity = platformActivityMapper.getById(platformDto.getId());
        if (ObjectUtil.isNotNull(activity)) {
            //修改
            activity.setCreateTime(DateUtils.parseDate(platformDto.getCreatetime(),DateUtils.DATE_TIME_PATTERN));
            activity.setOverTime(DateUtils.parseDate(platformDto.getOvertime(),DateUtils.DATE_TIME_PATTERN));
            activity.setUpdateTime(new Date());
            activity.setActivityName(platformDto.getActivityName());
            activity.setActivityExplain(platformDto.getActivityExplain());
            activity.setIsActivity(platformDto.getIsActivity());
            activity.setIsGift(platformDto.getIsGift());
            if (platformDto.getIsGift() == 0) {
                activity.setNums(platformDto.getNums());
            } else {
                activity.setGiftAmount(platformDto.getGiftAmount());
                activity.setManAmount(platformDto.getManAmount());
            }
            boolean updateFlag = platformActivityService.updateById(activity);
            return updateFlag;
        } else {
            //新增
            platformActivity = new TbPlatformActivity();
            platformActivity.setCreateTime(DateUtils.parseDate(platformDto.getCreatetime(),DateUtils.DATE_TIME_PATTERN));
            platformActivity.setOverTime(DateUtils.parseDate(platformDto.getOvertime(),DateUtils.DATE_TIME_PATTERN));
            platformActivity.setActivityExplain(platformDto.getActivityExplain());
            platformActivity.setIsActivity(platformDto.getIsActivity());
            platformActivity.setIsGift(platformDto.getIsGift());
            if (platformDto.getIsGift() == 0) {
                platformActivity.setNums(platformDto.getNums());
            } else {
                platformActivity.setGiftAmount(platformDto.getGiftAmount());
                platformActivity.setManAmount(platformDto.getManAmount());
            }
           // platformActivity.setOverTime(platformDto.getOverTime());
            platformActivity.setActivityName(platformDto.getActivityName());
            boolean saveFlag = platformActivityService.save(platformActivity);
            return saveFlag;
        }
    }

    /**
     * 获取活动列表
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    @Override
    public IPage<TbPlatformActivity> getActivityList(Integer pageNo, Integer pageSize, String keyword) {
        log.info("活动列表传来的参数：pageNo->{},pageSize->{},keyword->{}",pageNo,pageSize,keyword);
        if (StrUtil.isBlank(keyword)) {
            return platformActivityService.page(new Page<>(pageNo,pageSize));
        } else {
            return platformActivityService.page(new Page<>(pageNo,pageSize),new QueryWrapper<TbPlatformActivity>().like("activity_name",keyword));
        }
    }

    /**
     * 获取活动详情
     * @param id
     * @return
     */
    @Override
    public TbPlatformActivity getActivityDetail(Integer id) {
        log.info("获取活动详情传来的参数：id->{}",id);
        return platformActivityService.getById(id);
    }
}
