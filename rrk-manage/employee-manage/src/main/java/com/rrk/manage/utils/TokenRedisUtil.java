package com.rrk.manage.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.dto.CaptchaSmsCode;
import com.rrk.common.modules.manage.dto.EmployeePartDto;
import com.rrk.common.modules.manage.dto.PartPermDto;
import com.rrk.common.modules.manage.entity.*;
import com.rrk.common.modules.user.dto.UserRedisDto;
import com.rrk.common.modules.user.entity.TbMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * token和redis结合使用的工具类
 */
@Component
@Slf4j
public class TokenRedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;



    /**
     * 请求头Authorization
     */
    public static final String REQUEST_HEADER = "Authorization";

    /**
     * 将token加入到redis黑名单中
     *
     * @param token
     */
    public void addBlackList(String token) {

        redisTemplate.opsForSet().add(RedisConstant.BLACK_LIST, token);

    }

    /**
     * 判断此token是否在黑名单中
     *
     * @param token
     * @return
     */
    public Boolean isBlackList(String token) {
        return redisTemplate.opsForSet().isMember(RedisConstant.BLACK_LIST, token);
    }

    /**
     * 将token和用户的基本信息保存到redis
     */
    public void addUserToRedis(TbEmployee employee, String token) {
        UserRedisDto dto = new UserRedisDto();

        dto.setUserId(employee.getEmployeeId());
        dto.setToken(token);
        dto.setUsername(employee.getUsername());
        //设置半个小时过期
        redisTemplate.opsForValue().set(token, JSON.toJSONString(dto), 30 * 60, TimeUnit.SECONDS);
    }

    /**
     * 判断token是否过期
     */
    public Long isToken(String token) {
        Object object = redisTemplate.opsForValue().get(token);
        Object o = redisTemplate.opsForValue().get(token);
        if (ObjectUtil.isNull(o)) {
            return null;
        }
        UserRedisDto userRedisDto = JSON.parseObject(o.toString(), UserRedisDto.class);
        return userRedisDto.getUserId();
    }

    /**
     * 根据请求头中的token获取用户id
     */
    public Long getUserId(HttpServletRequest request) {
        String token = request.getHeader(REQUEST_HEADER);
        if (StrUtil.isBlank(token)) {
            return null;
        }
        Object o = redisTemplate.opsForValue().get(token);
        if (ObjectUtil.isNull(o)) {
            return null;
        }
        UserRedisDto userRedisDto = JSON.parseObject(o.toString(), UserRedisDto.class);
        return userRedisDto.getUserId();
    }

    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getUsernameByToken(String token) {
        return redisTemplate.opsForHash().get(token, "username");
    }

    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getIPByToken(String token) {
        return redisTemplate.opsForHash().get(token, "ip");
    }

    /**
     * 将所有的菜单配置信息放到redis(三天更新一次)
     */
    public void addMenuList(List<TbMenu> list) {
        redisTemplate.opsForValue().set(RedisConstant.MENU_CACHE, JSON.toJSONString(list), 1, TimeUnit.DAYS);
    }

    public void addPermissionList(List<TbPermission> list) {
        redisTemplate.opsForValue().set(RedisConstant.PERMISSION_CACHE, JSON.toJSONString(list), 1, TimeUnit.DAYS);
    }

    /**
     * 把所有的菜单配置信息获取
     */
    public List<TbMenu> getMenuList() {
        String s = redisTemplate.opsForValue().get(RedisConstant.MENU_CACHE);
        if (StrUtil.isBlank(s)) {
            return null;
        }
        List<TbMenu> menuList = JSON.parseArray(s, TbMenu.class);
        return menuList;
    }

    /**
     * 更新菜单缓存数据
     */
    public void updateMenu(Long partId, List<TbPermission> permissions) {
        List<PartPermDto> partPermDtos = new ArrayList<>();
        if (CollUtil.isNotEmpty(permissions)) {
            for (TbPermission permission : permissions) {
                PartPermDto partPermDto = new PartPermDto(partId,permission);
                partPermDtos.add(partPermDto);
            }
        }
        String s = redisTemplate.opsForValue().get(RedisConstant.PERMISSION_CACHE);
        if (StrUtil.isNotBlank(s)) {
            List<PartPermDto> partPermDtoList = JSON.parseArray(s, PartPermDto.class);
            for (PartPermDto partPermDto : partPermDtos) {
                PartPermDto permDto = partPermDtoList.stream().filter(p -> partPermDto.getPartId().longValue() == p.getPartId().longValue()
                        && partPermDto.getPermissionId().longValue() == p.getPermissionId().longValue()).findFirst().orElse(null);
                if (ObjectUtil.isNotNull(permDto)) {
                        Collections.replaceAll(partPermDtoList,  permDto,partPermDto);
                } else {
                    partPermDtoList.add(partPermDto);
                }
            }
            redisTemplate.opsForValue().set(RedisConstant.PERMISSION_CACHE, JSON.toJSONString(partPermDtoList));
        } else {
            redisTemplate.opsForValue().set(RedisConstant.PERMISSION_CACHE, JSON.toJSONString(partPermDtos));
        }
    }

    /**
     * 获取所有员工的权限配置信息
     */
    public List<TbPermission> getPermission() {
        String s = redisTemplate.opsForValue().get(RedisConstant.PERMISSION_CACHE);
        if (StrUtil.isBlank(s)) {
            return null;
        }
        List<TbPermission> permissions = JSON.parseArray(s, TbPermission.class);
        return permissions;
    }

    /**
     * 短信登录
     */
    public void saveSmsCode(CaptchaSmsCode smsCode) {
        redisTemplate.opsForValue().set(RedisConstant.SMS_CODE + smsCode.getMobile(), JSON.toJSONString(smsCode), 60 * 30, TimeUnit.SECONDS);

    }

    /**
     * 获取短信和手机号
     */
    public CaptchaSmsCode getSmsCode(String mobile) {
        String s = redisTemplate.opsForValue().get(RedisConstant.SMS_CODE + mobile);
        if (StrUtil.isBlank(s)) {
            return null;
        }
        CaptchaSmsCode smsCode = JSON.parseObject(s.toString(), CaptchaSmsCode.class);
        return smsCode;
    }

    /**
     * 查询token下的过期时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getExpirationTimeByToken(String token) {
        return redisTemplate.opsForHash().get(token, "expirationTime");

    }

    /**
     * 将角色信息保存到redis
     *
     * @param parts
     */
    public void editRole( List<TbPart> parts, Long userId) {
        List<EmployeePartDto> list = new ArrayList<>();
        for (TbPart part : parts) {
            EmployeePartDto partDto = new EmployeePartDto(part, userId);
            list.add(partDto);
        }
        String partStr = redisTemplate.opsForValue().get(RedisConstant.PART_CACHE);
        if (StrUtil.isNotBlank(partStr)) {
            List<EmployeePartDto> employeePartDtos = JSON.parseArray(partStr, EmployeePartDto.class);
            for (EmployeePartDto employeePartDto : list) {
                EmployeePartDto employeePart = employeePartDtos.stream().filter(p -> employeePartDto.getPartId().longValue() == employeePartDto.getPartId().longValue() && employeePartDto.getUserId().longValue() == p.getUserId().longValue()).findFirst().orElse(null);
                if (ObjectUtil.isNotNull(employeePart)) {
                    //不为空就替换
                    Collections.replaceAll(employeePartDtos, employeePart, employeePartDto);
                } else {
                    //新增
                    employeePartDtos.add(employeePartDto);
                }
            }

            redisTemplate.opsForValue().set(RedisConstant.PART_CACHE, JSON.toJSONString(employeePartDtos));
        } else {
            //新增角色
            List<EmployeePartDto> employeePartDtos = new ArrayList<>();
            employeePartDtos.addAll(list);
            redisTemplate.opsForValue().set(RedisConstant.PART_CACHE, JSON.toJSONString(employeePartDtos));
        }

    }

    /**
     * 批量删除缓存的数据
     *
     * @param partIds
     */
    public void delRole(List<Long> partIds) {
        String partStr = redisTemplate.opsForValue().get(RedisConstant.PART_CACHE);
        if (StrUtil.isNotBlank(partStr)) {
            List<TbPart> parts = JSON.parseArray(partStr, TbPart.class);
            List<TbPart> tbParts = parts.stream().filter(p -> !partIds.contains(p.getPartId())).collect(Collectors.toList());
            redisTemplate.opsForValue().set(RedisConstant.PART_CACHE, JSON.toJSONString(tbParts));
        }
    }

    /**
     * 初始化权限
     *
     * @param list
     */
    public void initPermission(List<TbPermission> list, List<TbPartPermission> partPermissions) {
        List<PartPermDto> partPermDtos = new ArrayList<>();
        for (TbPartPermission partPermission : partPermissions) {
             List<TbPermission> tbPermissions = list.stream().filter(p -> partPermission.getPermissionId().longValue() == p.getPermissionId().longValue()).collect(Collectors.toList());
            for (TbPermission permission : tbPermissions) {
                 PartPermDto partPermDto = new PartPermDto(partPermission.getPartId(), permission);
                partPermDtos.add(partPermDto);
            }
        }
        redisTemplate.opsForValue().set(RedisConstant.PERMISSION_CACHE, JSON.toJSONString(partPermDtos));
    }

    public void initPart(List<TbPart> tbParts,List<TbEmployeePart> employeeParts) {
        List<EmployeePartDto> employeePartDtos = new ArrayList<>();
        for (TbEmployeePart employeePart : employeeParts) {
             List<TbPart> parts = tbParts.stream().filter(p -> employeePart.getPartId().longValue() == p.getPartId().longValue()).collect(Collectors.toList());
            for (TbPart part : parts) {
                EmployeePartDto employeePartDto = new EmployeePartDto(part,employeePart.getEmployeeId());
                employeePartDtos.add(employeePartDto);
            }
        }
        redisTemplate.opsForValue().set(RedisConstant.PART_CACHE, JSON.toJSONString(employeePartDtos));
    }

//    public void setTokenRefresh(String token, String username, String ip) {
//        //刷新时间
//        Integer expire = validTime * 24 * 60 * 60 * 1000;
//        redisUtil.hset(username, "token", token, expire);
//        redisUtil.hset(username, "tokenValidTime", DateUtil.getAddDayTime(validTime), expire);
//        redisUtil.hset(username, "expirationTime", DateUtil.getAddDaySecond(expirationSeconds), expire);
//        redisUtil.hset(username, "username", username, expire);
//        redisUtil.hset(username, "ip", ip, expire);
//    }

}
