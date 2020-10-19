package com.rrk.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.manage.entity.TbPart;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-04-25
 */
public interface ITbPartService extends IService<TbPart> {

    /**
     * 获取角色列表
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    IPage<TbPart> getPartList(Integer pageNo, Integer pageSize, String keyword);

    /**
     * 批量删除角色列表
     * @param partIds
     * @return
     */
    Integer delPart(List<Long> partIds);
}
