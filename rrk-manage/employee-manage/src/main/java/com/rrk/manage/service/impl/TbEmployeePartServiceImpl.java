package com.rrk.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.manage.dao.TbEmployeePartMapper;
import com.rrk.common.modules.manage.entity.TbEmployeePart;
import com.rrk.manage.service.ITbEmployeePartService;
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
public class TbEmployeePartServiceImpl extends ServiceImpl<TbEmployeePartMapper, TbEmployeePart> implements ITbEmployeePartService {

}
