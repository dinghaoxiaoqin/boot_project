package com.rrk.gateway.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.rrk.common.constant.RedisConstant;
import com.rrk.common.dto.CaptchaSmsCode;
import com.rrk.common.modules.manage.dto.EmployeePartDto;
import com.rrk.common.modules.manage.dto.PartPermDto;
import com.rrk.common.modules.manage.entity.TbEmployee;
import com.rrk.common.modules.manage.entity.TbPart;
import com.rrk.common.modules.manage.entity.TbPermission;
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



//    @Autowired
//    private RedisTemplate redisTemplate;

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
        redisTemplate.opsForValue().set(RedisConstant.MENU_CACHE, JSON.toJSONString(list),1,TimeUnit.DAYS);
    }

    public void addPermissionList(List<TbPermission> list) {
        redisTemplate.opsForValue().set(RedisConstant.PERMISSION_CACHE, JSON.toJSONString(list),1,TimeUnit.DAYS);
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
    public void updateMenu(TbPermission permission){
         String s = redisTemplate.opsForValue().get(RedisConstant.PERMISSION_CACHE);
        if (StrUtil.isNotBlank(s)) {
             List<TbPermission> permissions = JSON.parseArray(s, TbPermission.class);
             TbPermission tbPermission = permissions.stream().filter(p -> permission.
                    getPermissionId().longValue() == p.getPermissionId().longValue()).findFirst().orElse(null);
            if (ObjectUtil.isNotNull(tbPermission)) {
                //替换元素
                Collections.replaceAll(permissions,tbPermission,permission);
            } else {
                permissions.add(permission);
            }
            redisTemplate.opsForValue().set(RedisConstant.PERMISSION_CACHE,JSON.toJSONString(permissions));
        } else {
            List<TbPermission> list = new ArrayList<>();
            list.add(permission);
            redisTemplate.opsForValue().set(RedisConstant.PERMISSION_CACHE,JSON.toJSONString(list));
        }
    }

    /**
     * 获取所有员工的权限配置信息
     */
    public List<PartPermDto> getPermission(){
        String s = redisTemplate.opsForValue().get(RedisConstant.PERMISSION_CACHE);
        if (StrUtil.isBlank(s)) {
            return null;
        }
        List<PartPermDto> permissions = JSON.parseArray(s, PartPermDto.class);
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
     * @param part
     */
    public void editRole(TbPart part) {
         String partStr = redisTemplate.opsForValue().get(RedisConstant.PART_CACHE);
        if (StrUtil.isNotBlank(partStr)) {
            List<TbPart> parts = JSON.parseArray(partStr, TbPart.class);
             TbPart tbPart = parts.stream().filter(p -> part.getPartId().longValue() == part.getPartId().longValue()).findFirst().orElse(null);
            if (ObjectUtil.isNotNull(tbPart)) {
                //不为空就替换
                Collections.replaceAll(parts,tbPart,part);
            } else {
                //新增
                parts.add(part);
            }
            redisTemplate.opsForValue().set(RedisConstant.PART_CACHE,JSON.toJSONString(parts));
        } else {
            //新增角色
            List<TbPart> list = new ArrayList<>();
            list.add(part);
            redisTemplate.opsForValue().set(RedisConstant.PART_CACHE,JSON.toJSONString(list));
        }

    }

    /**
     * 批量删除缓存的数据
     * @param partIds
     */
    public void delRole(List<Long> partIds) {
        String partStr = redisTemplate.opsForValue().get(RedisConstant.PART_CACHE);
        if (StrUtil.isNotBlank(partStr)) {
            List<TbPart> parts = JSON.parseArray(partStr, TbPart.class);
            List<TbPart> tbParts = parts.stream().filter(p -> !partIds.contains(p.getPartId())).collect(Collectors.toList());
            redisTemplate.opsForValue().set(RedisConstant.PART_CACHE,JSON.toJSONString(tbParts));
        }
    }

    /**
     * 初始化权限
     * @param list
     */
    public void initPermission(List<TbPermission> list) {
        redisTemplate.opsForValue().set(RedisConstant.PERMISSION_CACHE,JSON.toJSONString(list));
    }

    public void initPart(List<TbPart> tbParts) {
        redisTemplate.opsForValue().set(RedisConstant.PART_CACHE,JSON.toJSONString(tbParts));
    }

    //获取角色信息
    public List<EmployeePartDto> getPartList(){
        String s = redisTemplate.opsForValue().get(RedisConstant.PART_CACHE);
        if (StrUtil.isBlank(s)) {
            return null;
        }
        List<EmployeePartDto> parts = JSON.parseArray(s, EmployeePartDto.class);
        return parts;
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
