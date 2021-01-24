package com.rrk.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.product.dao.TbActivityProductMapper;
import com.rrk.common.modules.product.entity.TbActivityProduct;
import com.rrk.product.service.ITbActivityProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dinghao
 * @since 2020-11-28
 */
@Service
@Transactional(rollbackFor = MyException.class)
@Slf4j
public class TbActivityProductServiceImpl extends ServiceImpl<TbActivityProductMapper, TbActivityProduct> implements ITbActivityProductService {

}
