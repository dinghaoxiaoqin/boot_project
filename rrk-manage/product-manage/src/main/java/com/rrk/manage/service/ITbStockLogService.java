package com.rrk.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.common.modules.product.entity.TbStockLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dinghao
 * @since 2020-09-09
 */
public interface ITbStockLogService extends IService<TbStockLog> {

    /**
     * 添加sku的库存日志
     * @param sku
     * @return
     */
    boolean addStockLog(TbSku sku,Long userId);
}
