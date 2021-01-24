package com.rrk.manage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rrk.common.R;
import com.rrk.common.modules.product.entity.TbPlatformActivity;
import com.rrk.common.utils.DateUtils;
import com.rrk.manage.dto.PlatformDto;
import com.rrk.manage.service.ITbPlatformActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * <p>
 *  平台活动前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@RestController
@RequestMapping("/activity")
@Slf4j
@CrossOrigin
public class TbPlatformActivityController {

    @Autowired
    private ITbPlatformActivityService platformActivityService;

    /**
     * 编辑活动
     */
    @PostMapping(value = "/addActivity")
    public R<Object> addActivity(@RequestBody PlatformDto platformDto) throws ParseException {
       Boolean flag =  platformActivityService.editActivity(platformDto);
       return flag == true? R.ok(200,"编辑成功",""): R.fail(500,"编辑失败");
    }

    //获取活动列表
    @GetMapping(value = "/getActivityList")
    public R<Object> getActivityList(@RequestParam(value = "pageNo") Integer pageNo,
                                     @RequestParam(value = "pageSize") Integer pageSize,
                                     @RequestParam(value = "keyword",required = false) String keyword ){
        IPage<TbPlatformActivity> page = platformActivityService.getActivityList(pageNo,pageSize,keyword);
        return R.ok(200,"操作成功",page);
    }
    //活动详情
    @GetMapping(value = "/getActivityDetail")
    public R<Object> getActivityDetail(@RequestParam(value = "id") Integer id){
        TbPlatformActivity platformActivity =   platformActivityService.getActivityDetail(id);
        PlatformDto platformDto = new PlatformDto();
        platformDto.setCreatetime(DateUtils.formatDate(platformActivity.getCreateTime(),DateUtils.DATE_TIME_PATTERN));
        platformDto.setOvertime(DateUtils.formatDate(platformActivity.getOverTime(),DateUtils.DATE_TIME_PATTERN));
        platformDto.setIsActivity(platformActivity.getIsActivity());
        platformDto.setIsGift(platformActivity.getIsGift());
        platformDto.setNums(platformActivity.getNums());
        platformDto.setActivityExplain(platformActivity.getActivityExplain());
        platformDto.setActivityName(platformActivity.getActivityExplain());
        platformDto.setGiftAmount(platformActivity.getGiftAmount());
        platformDto.setManAmount(platformActivity.getManAmount());
        return R.ok(200,"操作成功",platformDto);
    }
    //编辑活动状态（上下架）
    @GetMapping(value = "/updateIsActivity")
    public R<Object> updateIsActivity(@RequestParam(value = "id") Long id,
                                      @RequestParam(value = "isActivity") Integer isActivity){
        TbPlatformActivity platformActivity = new TbPlatformActivity();
        platformActivity.setId(id);
        platformActivity.setIsActivity(isActivity);
        boolean b = platformActivityService.updateById(platformActivity);
        if (b) {
            return R.ok(200,"操作成功","");
        } else {
            return R.fail(400,"操作失败");
        }
    }

    /**
     * 删除活动
     */
    @PostMapping(value = "/deleteActivity")
    public R<Object> deleteActivity(@RequestParam(value = "id") Long id){
        boolean b = platformActivityService.removeById(id);
        if (b) {
            return R.ok(200,"删除成功","");
        } else {
            return R.fail(400,"删除失败");
        }
    }
}

