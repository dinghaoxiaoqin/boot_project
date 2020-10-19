package com.rrk.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.manage.dao.TbEmployeePartMapper;
import com.rrk.common.modules.manage.dao.TbPartMapper;
import com.rrk.common.modules.manage.dao.TbPartPermissionMapper;
import com.rrk.common.modules.manage.entity.TbEmployeePart;
import com.rrk.common.modules.manage.entity.TbPart;
import com.rrk.common.modules.manage.entity.TbPartPermission;
import com.rrk.manage.service.ITbPartService;
import com.rrk.manage.utils.TokenRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-04-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbPartServiceImpl extends ServiceImpl<TbPartMapper, TbPart> implements ITbPartService {

    @Autowired
    private ITbPartService partService;

    @Autowired
    private TbEmployeePartMapper employeePartMapper;

    @Autowired
    private TbPartPermissionMapper partPermissionMapper;
    @Autowired
    private TokenRedisUtil tokenRedisUtil;

    @Override
    public IPage<TbPart> getPartList(Integer pageNo, Integer pageSize, String keyword) {
         Page<TbPart> page = new Page<TbPart>(pageNo, pageSize);
        if (StrUtil.isBlank(keyword)) {
             IPage<TbPart> pagePart = partService.page(page);
             return pagePart;
        } else {
            IPage<TbPart> pagePart = partService.page(page,new QueryWrapper<TbPart>().eq("part_name",keyword));
            return pagePart;
        }
    }

    /**
     * 批量删除角色
     * @param partIds
     * @return
     */
    @Override
    public Integer delPart(List<Long> partIds) {
        if (CollUtil.isNotEmpty(partIds)) {
             boolean b = partService.removeByIds(partIds);
           // tokenRedisUtil.delRole(partIds);
            for (Long partId : partIds) {
                 int delete = employeePartMapper.delete(new QueryWrapper<TbEmployeePart>().eq("part_id", partId));
                 int delPermission = partPermissionMapper.delete(new QueryWrapper<TbPartPermission>().eq("part_id", partId));
            }
            return b? 1:0;
        }
        return 1;
    }
}
