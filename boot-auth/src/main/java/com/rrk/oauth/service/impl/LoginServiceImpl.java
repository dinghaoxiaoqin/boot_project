package com.rrk.oauth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.dto.SmsLoginDto;
import com.rrk.common.handle.LoginException;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.commonConstant.CommonContants;
import com.rrk.common.modules.manage.dao.TbEmployeeMapper;
import com.rrk.common.modules.manage.dto.EmployeeDto;
import com.rrk.common.modules.manage.entity.TbEmployee;
import com.rrk.common.modules.user.entity.OauthClientDetails;
import com.rrk.oauth.config.auth.MyAuthenticationManager;
import com.rrk.oauth.config.tokenConfig.SmsCodeToken;
import com.rrk.oauth.config.tokenConfig.SocialAuthenticationToken;
import com.rrk.oauth.service.LoginService;
import com.rrk.oauth.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @author dinghao
 * @date 2020 6-26
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbEmployeeMapper employeeMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private MyClientDetailsService myClientDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MyAuthenticationManager authenticationManager;
    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    @Autowired
    private TokenStore tokenStore;

    private static final String MOBILE_REG = "^[1][3578][0-9]{9}$";

    /**
     * 请求头Authorization
     */
    public static final String REQUEST_HEADER = "Authorization";

    /**
     * 后台用户登录
     *
     * @param employeeDto
     * @return
     */
    @Override
    public String login(EmployeeDto employeeDto) {
        if (StrUtil.isBlank(employeeDto.getUsername())) {
            throw new MyException("用户名不存在");
        }
        if (StrUtil.isBlank(employeeDto.getPassword())) {
            throw new MyException("密码不存在");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(employeeDto.getUsername(), employeeDto.getPassword());
        OAuth2Request oAuth2Request = getOauth2Request(CommonContants.SMS_OR_PASSWORD);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //通过security+oauth2生成token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);
        String tokenValue = oAuth2AccessToken.getValue();
        return tokenValue;
    }

    private OAuth2Request
    getOauth2Request(Integer id) {
        OauthClientDetails clientDetail = myClientDetailsService.getClientById(id);
        ClientDetails clientDetails = getClient(clientDetail.getClientId(), clientDetail.getResourceIds());
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientDetail.getClientId(), clientDetails.getScope(),
                clientDetail.getAuthorizedGrantTypes());
        return tokenRequest.createOAuth2Request(clientDetails);
    }

    /**
     * 获取密钥相关数据
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    private ClientDetails getClient(String clientId, String clientSecret) {
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else {
            if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
                throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
            }
        }
        return clientDetails;
    }

    /**
     * 用户登出操作
     *
     * @param request
     * @return
     */
    @Override
    public Integer logout(HttpServletRequest request) {
        try {
            //获取token
            String token = request.getHeader(REQUEST_HEADER);
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            //清空token
            tokenStore.removeAccessToken(oAuth2AccessToken);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 手机验证码登录
     *
     * @param smsLoginDto
     * @return
     */
    @Override
    public String smsLogin(SmsLoginDto smsLoginDto) {
        //1对手机号和验证码进行验证
        checkSmsLogin(smsLoginDto);
        SmsCodeToken smsCodeToken = new SmsCodeToken(smsLoginDto.getMobile());
        OAuth2Request oAuth2Request = getOauth2Request(CommonContants.SMS_OR_PASSWORD);
        Authentication authentication = authenticationManager.authenticate(smsCodeToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);
        String tokenValue = oAuth2AccessToken.getValue();
        return tokenValue;
    }

    /**
     * 移动端手机短信登录
     *
     * @param smsLoginDto
     * @return
     */
    @Override
    public String webSmsLogin(SmsLoginDto smsLoginDto) {
        checkSmsLogin(smsLoginDto);
        SmsCodeToken smsCodeToken = new SmsCodeToken(smsLoginDto.getMobile());
        OAuth2Request oAuth2Request = getOauth2Request(CommonContants.SMS_OR_PASSWORD);
        Authentication authentication = authenticationManager.appAuthenticate(smsCodeToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);
        //生成的token
        String tokenValue = oAuth2AccessToken.getValue();
        //将token保存到redis
        redisUtil.addToken(tokenValue);
        return tokenValue;
    }

    /**
     * 第三方登录（社交登录）
     *
     * @param code
     * @return
     */
    @Override
    public String appLogin(String code, Integer id) {
        //1,封装授权码登录认证的实体
        SocialAuthenticationToken socialAuthenticationToken = new SocialAuthenticationToken(code,id);
        //2,从数据库获取与第三方达成的协议密钥数据
        OAuth2Request oauth2Request = getOauth2Request(id);
        //3.认证管理器对授权认证码进行认证
        Authentication authentication = authenticationManager.socialAuthenticate(socialAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oauth2Request, authentication);
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);
        String tokenValue = oAuth2AccessToken.getValue();
        //将token保存到redis
        redisUtil.addToken(tokenValue);
        return tokenValue;
    }

    /**
     * 对手机号和验证码进行验证
     *
     * @param smsLoginDto
     */
    private void checkSmsLogin(SmsLoginDto smsLoginDto) {
        if (StrUtil.isBlank(smsLoginDto.getMobile())) {
            throw new LoginException(437, "请输入手机号");
        }
        if (StrUtil.isBlank(smsLoginDto.getSmsCode())) {
            throw new LoginException(437, "请输入验证码");
        }
        //手机号是否正确
        if (!Pattern.compile(MOBILE_REG).matcher(smsLoginDto.getMobile()).matches()) {
            throw new LoginException(437, "手机号输入错误");
        }
        String code = redisUtil.getCode(smsLoginDto.getMobile());
        if (StrUtil.isBlank(code)) {
            throw new LoginException(437, "验证码已过期");
        } else {
            if (!StrUtil.equals(smsLoginDto.getSmsCode(), code)) {
                throw new LoginException(437, "验证码输入错误");
            }
        }
        TbEmployee employee = employeeMapper.selectOne(new QueryWrapper<TbEmployee>().eq("phone", smsLoginDto.getMobile()));
        if (ObjectUtil.isNull(employee)) {
            throw new LoginException(437, "该手机号还不是后台系统用户，请联系管理员");
        }
    }
}
