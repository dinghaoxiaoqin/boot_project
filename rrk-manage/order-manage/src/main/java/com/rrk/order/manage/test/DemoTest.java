package com.rrk.order.manage.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.modules.order.entity.TbOrder;
import com.rrk.common.modules.user.entity.TbRegion;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.order.manage.dto.KdNiaoDto;
import com.rrk.order.manage.service.ITbOrderService;
import com.rrk.order.manage.service.ITbRegionService;
import com.rrk.order.manage.service.ITbUserService;
import com.rrk.order.manage.utils.KdNiaoUtil;
import com.rrk.order.manage.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoTest {


    @Autowired
    private ITbOrderService orderService;

    @Autowired
    private ITbUserService userService;

    @Autowired
    private ITbRegionService regionService;

    @Autowired
    private RedisUtil redisUtil;


    @Test
    public void test01(){
        //System.out.println("测试");
        TbOrder byId = orderService.getById("5307");
        System.out.println("订单的数据："+byId);
        //获取用户数据
        TbUser user = getUser();
        System.out.println("用户的数据："+user);
    }

 //   @DataSource(value = DatasourceEnum.BOOT)
    private TbUser getUser() {
        TbUser user = userService.getOne(new QueryWrapper<TbUser>().eq("user_id", 730578624L));
        System.out.println("数据："+user);
     return userService.getById(24L);
    }

    @Test
    public void test02(){
        String partName = redisUtil.getPartName(1L);
        System.out.println(partName);
    }

    /**
     * 测试物流数据
     */
    @Test
    public void test03(){
        String shipMent = "YTO";
        String expressNo = "YT9255445187142";
        String orderTracesByJson = KdNiaoUtil.getOrderTracesByJson(shipMent, expressNo);
        KdNiaoDto kdNiaoDto = JSON.parseObject(orderTracesByJson, KdNiaoDto.class);
        System.out.println(kdNiaoDto);
    }

    @Test
    public void test04(){
        List<TbRegion> level = regionService.list(new QueryWrapper<TbRegion>().eq("level", 2));
        StringBuffer buffer = new StringBuffer();
        for (TbRegion region : level) {
           buffer.append("'").append(region.getName()).append("'")
                   .append(":").append("[").append(region.getLng()).append(",")
                   .append(region.getLat()).append("]").append(",");
        }
        String s = buffer.toString();
        System.out.println("获取的数据："+s);
    }
}
