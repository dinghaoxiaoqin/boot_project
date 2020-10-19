package com.rrk.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.order.dao.TbPayLogMapper;
import com.rrk.common.modules.order.entity.TbPayLog;
import com.rrk.order.service.ITbPayLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbPayLogServiceImpl extends ServiceImpl<TbPayLogMapper, TbPayLog> implements ITbPayLogService {

}
