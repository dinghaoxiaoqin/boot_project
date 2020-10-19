package com.rrk.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.manage.dao.TbPartPermissionMapper;
import com.rrk.common.modules.manage.entity.TbPartPermission;
import com.rrk.manage.service.ITbPartPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class TbPartPermissionServiceImpl extends ServiceImpl<TbPartPermissionMapper, TbPartPermission> implements ITbPartPermissionService {

}
