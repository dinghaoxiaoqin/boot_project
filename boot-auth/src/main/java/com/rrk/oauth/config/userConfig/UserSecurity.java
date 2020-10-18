package com.rrk.oauth.config.userConfig;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.rrk.common.modules.manage.entity.TbEmployee;
import com.rrk.common.modules.manage.entity.TbPart;
import com.rrk.common.modules.manage.entity.TbPermission;
import com.rrk.common.modules.user.entity.TbUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户的安全认证 和权限处理的类
 */
public class UserSecurity extends TbEmployee implements UserDetails {

    /**
     * 为了区分权限和角色在角色前面加"ROLE_"前缀
     */
    private static final String ROLE = "ROLE_";

    private String username;

    private String token;




    private List<TbPart> partList;

    private List<TbPermission> permissions;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 无参构造
     */
    public UserSecurity() {
    }

    /**
     * 含参数构造
     */
//    public UserSecurity(TbEmployee employee, List<TbPart> partList) {
//        if (employee != null) {
//            this.setCreateTime(new Date());
//            this.setPassword(employee.getPassword());
//            this.setEmployeeId(employee.getEmployeeId());
//            this.setUsername(employee.getUsername());
//            this.username = employee.getUsername();
//        }
//        if (!partList.isEmpty()) {
//            this.partList = partList;
//        }
//
//    }

    public UserSecurity(TbEmployee employee, List<TbPart> partList, List<TbPermission> tbPermissions) {
        if (employee != null) {
            this.setCreateTime(new Date());
            this.setPassword(employee.getPassword());
            this.setEmployeeId(employee.getEmployeeId());
            this.setUsername(employee.getUsername());
            this.username = employee.getUsername();
        }
        if (!partList.isEmpty()) {
            this.partList = partList;
        }
        if (CollUtil.isNotEmpty(tbPermissions)) {
            this.permissions = tbPermissions;
        }
    }

    public UserSecurity(TbUser tbUser) {
        this.setCreateTime(new Date());
        this.username = tbUser.getUsername();
        this.setPassword(tbUser.getPassword());
        this.setUsername(tbUser.getUsername());
       this.setEmployeeId(tbUser.getUserId());
    }

    /**
     * 添加用户所拥有的权限和角色
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollUtil.isNotEmpty(authorities)) {
            return authorities;
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        //添加角色
        if (!partList.isEmpty()) {
            for (TbPart part : partList) {
                if (part != null) {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(part.getPartCode());
                    authorityList.add(simpleGrantedAuthority);
                }
            }
        }
        //添加权限
        if (CollUtil.isNotEmpty(permissions)) {
            for (TbPermission permission : permissions) {
                if (ObjectUtil.isNotNull(permission)) {
                    if (StrUtil.isNotBlank(permission.getPermissionUrl())) {
                        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission.getPermissionUrl());
                        authorityList.add(simpleGrantedAuthority);
                    }
                }
            }
        }
        return authorityList;
    }

    /**
     * 用户名称
     *
     * @return
     */

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 登录是否过期
     *
     * @return
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 登录是否锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 是否可用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
