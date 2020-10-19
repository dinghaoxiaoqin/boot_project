package com.rrk.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.modules.product.dao.TbStockLogMapper;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.common.modules.product.entity.TbStockLog;
import com.rrk.manage.service.ITbStockLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TbStockLogServiceImpl extends ServiceImpl<TbStockLogMapper, TbStockLog> implements ITbStockLogService {

    @Autowired
    private ITbStockLogService stockLogService;

    /**
     * 添加sku的日志
     * @param sku
     * @return
     */
    @Override
    public boolean addStockLog(TbSku sku,Long userId) {
        TbStockLog tbStockLog = new TbStockLog();
        tbStockLog.setCreateTime(new Date());
        tbStockLog.setEmployeeId(userId);
        tbStockLog.setSkuId(sku.getId());
        tbStockLog.setOperateType("编辑库存");
        tbStockLog.setStock(sku.getStock());
        return stockLogService.save(tbStockLog);
    }
}
