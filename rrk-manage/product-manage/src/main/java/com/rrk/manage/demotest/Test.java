package com.rrk.manage.demotest;

import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.manage.service.ITbSpuService;
import com.rrk.manage.service.ITbStockLogService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
//@Transactional(rollbackFor = Exception.class)
public class Test {

    @Autowired
    private ITbSpuService spuService;

    @Autowired
    private ITbStockLogService stockLogService;

    /**
     * 测试本地事务
     */

    //@Transactional
    @org.junit.Test
    public void test01(){
        TbSku sku = new TbSku();
        sku.setImages("111");
        sku.setCreateTime(new Date());
        sku.setSpuId(111L);
        sku.setTitle("测试");
        sku.setEnable(1);
        sku.setIsVip(0);
        sku.setOwnSpec("1122");
        sku.setPrice(BigDecimal.valueOf(12));
        sku.setSalePrice(BigDecimal.valueOf(10));
        sku.setStock(20);
        boolean save =  spuService.addSku(sku,116L);
        if (save ) {
            System.out.println("操作成功");
        }
    }
}
