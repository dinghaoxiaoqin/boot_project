package com.rrk.order.manage.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.modules.manage.dto.EmployeePartDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 根据用户id获取用户角色信息
     */
    public String getPartName(Long userId) {
        String partStr = redisTemplate.opsForValue().get(RedisConstant.PART_CACHE);
        if (StrUtil.isNotBlank(partStr)) {
            List<EmployeePartDto> employeePartDtos = JSON.parseArray(partStr, EmployeePartDto.class);
            EmployeePartDto dto = employeePartDtos.stream().filter(e -> userId.longValue() == e.getUserId().longValue()).findFirst().orElse(null);
            return dto.getPartCode();
        }
        return null;
    }
}
