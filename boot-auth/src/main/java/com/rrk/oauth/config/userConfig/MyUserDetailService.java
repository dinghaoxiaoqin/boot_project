package com.rrk.oauth.config.userConfig;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.constant.NumConstants;
import com.rrk.common.constant.UserContants;
import com.rrk.common.handle.LoginException;
import com.rrk.common.modules.manage.dao.*;
import com.rrk.common.modules.manage.entity.*;
import com.rrk.common.modules.user.dao.OauthClientDetailsMapper;
import com.rrk.common.modules.user.dao.TbUserMapper;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.common.utils.ToolUtil;
import com.rrk.oauth.annotation.SocialStragety;
import com.rrk.oauth.service.SmsCodeUserDetailsService;
import com.rrk.oauth.service.SocialHandleService;
import com.rrk.oauth.service.SocialUserDetailsService;
import com.rrk.oauth.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*****
 * @author dinghao
 * 由于我们自定义认证逻辑，所以需要重写UserDetailService
 */
@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService, SmsCodeUserDetailsService, SocialUserDetailsService {

    @Autowired
    private RedisUtil tokenRedisUtil;
    @Autowired
    private TbEmployeeMapper employeeMapper;
    @Autowired
    private TbEmployeePartMapper employeePartMapper;
    @Autowired
    private TbPartMapper partMapper;
    @Autowired
    private TbPartPermissionMapper partPermissionMapper;
    @Autowired
    private TbPermissionMapper permissionMapper;
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;
    private static final String MOBILE_REG = "^[1][3578][0-9]{9}$";

    /****
     * 自定义授权方式
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserSecurity loadUserByUsername(String username) throws UsernameNotFoundException {
        TbEmployee employee = new TbEmployee();
        //校验username是否为手机号
        boolean flag = isMobile(username);
        if (flag) {
            //后台手机登录
            employee = employeeMapper.selectOne(new QueryWrapper<TbEmployee>().eq("phone", username.substring(username.indexOf(",") + 1)));
        } else {
            //后台账号密码登录
            employee = employeeMapper.selectOne(new QueryWrapper<TbEmployee>().eq("username", username));
        }
        if (ObjectUtil.isNull(employee)) {
            throw new LoginException(437, "用户:" + username + "不存在");
        } else {
            if (employee.getEmployeeId() == 1) {
                BCryptPasswordEncoder en = new BCryptPasswordEncoder();
                String password = en.encode(employee.getPassword());
                employee.setPassword(password);
            }
            List<TbPart> partList = new ArrayList<>();
            List<TbPermission> tbPermissions = new ArrayList<>();
            //3.获取用户的角色和权限
            List<TbEmployeePart> employeePartList = employeePartMapper.selectList(new QueryWrapper<TbEmployeePart>().eq("employee_id", employee.getEmployeeId()));
            if (ObjectUtil.isNotNull(employeePartList)) {
                List<Long> collect = employeePartList.stream().map(u -> u.getPartId()).collect(Collectors.toList());
                partList = partMapper.selectList(new QueryWrapper<TbPart>().in("part_id", collect));
                List<TbPartPermission> tbPartPermissions = partPermissionMapper.selectList(new QueryWrapper<TbPartPermission>().in("part_id", collect));
                if (CollUtil.isNotEmpty(tbPartPermissions)) {
                    tbPermissions = permissionMapper.selectList(new QueryWrapper<TbPermission>().in("permission_id", tbPartPermissions.stream().map(p -> p.getPermissionId()).collect(Collectors.toList())));
                }
            }
            UserSecurity security = new UserSecurity(employee, partList, tbPermissions);
            return security;
        }
    }

    /**
     * 校验是否为手机号
     *
     * @param username
     * @return
     */
    private boolean isMobile(String username) {
        Pattern compile = Pattern.compile(MOBILE_REG);
        Matcher matcher = compile.matcher(username);
        boolean matches = matcher.matches();
        return matches;
    }

    /**
     * 移动端手机登录
     *
     * @param mobile
     * @return
     */
    @Override
    public UserSecurity loadUserByMobile(String mobile) {
        TbUser user = userMapper.selectOne(new QueryWrapper<TbUser>().eq("phone", mobile));
        if (ObjectUtil.isNull(user)) {
            //为空当做用户注册
            user = new TbUser();
            user.setNickName(UserContants.DEFAULT_PRE + ToolUtil.getRandowStr());
            user.setUsername(UserContants.DEFAULT_PRE + ToolUtil.getRandowStr());
            user.setPhone(mobile);
            user.setCreateTime(new Date());
            user.setUserRank(UserContants.NOMAL_ROLE);
            user.setEnabled(UserContants.USER_ENABLE);
            user.setUserId(ToolUtil.getLongNum());
            user.setSourceType(NumConstants.SOURCE_MOBILE);
            userMapper.insert(user);
        }
        UserSecurity security = new UserSecurity(user);
        return security;
    }

    /**
     * 第三方社交登录
     *
     * @param authorizationCode
     * @param socialId
     * @return
     */
    @Override
    public UserSecurity loadUserByAuthorizationCode(String authorizationCode, Integer socialId) {
        log.info("第三方登录传入的参数：authorizationCode->{},socialId->{}", authorizationCode, socialId);
         SocialHandleService socialHandleService = SocialStragety.getSocialHandleService(socialId);
         UserSecurity userSecurity = socialHandleService.getUserSecurity(socialId,authorizationCode);
         return userSecurity;
    }
}
