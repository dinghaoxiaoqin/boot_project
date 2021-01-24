package com.rrk.order.fegin.fallback;

import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.rrk.common.constant.FeginConstant;
import com.rrk.common.modules.user.dto.webdto.RegionDto;
import com.rrk.common.modules.user.entity.TbUserAddress;
import com.rrk.order.fegin.UserFeginClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 调用用户微服务降级处理
 */
@Component
@Slf4j
public class UserFallbackFactory implements FallbackFactory<UserFeginClient> {
    @Override
    public UserFeginClient create(Throwable throwable) {
       return new UserFeginClient() {
           @Override
           public TbUserAddress getUserAddressById(Long userId, Long addressId) {
               TbUserAddress tbUserAddress = new TbUserAddress();
               if (throwable instanceof FlowException) {
                   log.error("用户服务->{}的->{}流控了---->{}", FeginConstant.USER_CLIENT_NAME,"getUserAddressById",throwable.getMessage());
                   tbUserAddress.setId(-1L);
               }else {
                   log.error("用户服务->{}的->{}接口降级---->{}", FeginConstant.USER_CLIENT_NAME,"getUserAddressById",throwable.getMessage());
                   tbUserAddress.setId(-2L);
               }
               return tbUserAddress;
           }
           @Override
           public RegionDto getRegions(){
               return new RegionDto();
           }
       };
    }
//    /**
//     * 获取用户地址服务降级
//     * @param userId
//     * @param addressId
//     * @return
//     */
//    @Override
//    public TbUserAddress getUserAddressById(Long userId, Long addressId) {
//        log.info("获取用户微服务->{}的->{}服务降级", FeginConstant.USER_CLIENT_NAME,"getUserAddressById");
//        return null;
//
//    }
}
