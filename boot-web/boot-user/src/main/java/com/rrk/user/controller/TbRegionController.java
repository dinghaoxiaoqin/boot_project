package com.rrk.user.controller;

import com.rrk.common.R;
import com.rrk.common.modules.user.dto.webdto.RegionDto;
import com.rrk.user.service.ITbRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dinghao
 * @since 2020-09-01
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class TbRegionController {

    @Autowired
    private ITbRegionService regionService;

    /**
     * 获取省市区（县）三级联动的数据
     */
    @GetMapping(value = "/getRegionList")
    public R<Object> getRegionList(HttpServletRequest request){
        RegionDto regionDto = regionService.getRegionList();
        return R.ok(200,"操作成功",regionDto);
    }

}

