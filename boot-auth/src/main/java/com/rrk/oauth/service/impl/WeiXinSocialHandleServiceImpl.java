package com.rrk.oauth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.commonConstant.CommonContants;
import com.rrk.common.modules.user.dao.OauthClientDetailsMapper;
import com.rrk.common.modules.user.dao.TbUserMapper;
import com.rrk.common.modules.user.entity.OauthClientDetails;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.common.utils.HttpClientUtil;
import com.rrk.oauth.annotation.SocialType;
import com.rrk.oauth.config.userConfig.UserSecurity;
import com.rrk.oauth.service.SocialHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dinghao
 * 处理微信登录的业务
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@SocialType(socialType = CommonContants.WEICHAT_ID)
public class WeiXinSocialHandleServiceImpl implements SocialHandleService {

    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;
    @Autowired
    private TbUserMapper userMapper;

    /**
     * 微信社交登录
     *
     * @param socialType
     * @param authorizationCode
     * @return
     */
    @Override
    public UserSecurity getUserSecurity(Integer socialType, String authorizationCode) {
        /**
         * https请求方式:
         * GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
         */
        OauthClientDetails clientDetail = oauthClientDetailsMapper.selectOne(new QueryWrapper<OauthClientDetails>().eq("id", socialType));
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("grant_type", clientDetail.getAuthorizedGrantTypes());
        paramMap.put("appid", clientDetail.getClientId());
        paramMap.put("secret", clientDetail.getResourceIds());
        String accessToken = HttpClientUtil.doGet(CommonContants.WEIXIN_TOKEN_URL, paramMap);
        log.info("获取第三方的用户数据：access_token->{}", accessToken);
        Map<String, String> hashMap = JSON.parseObject(accessToken, HashMap.class);
        if (StrUtil.isNotBlank(hashMap.get("errcode"))) {
            throw new MyException("微信授权登录失败");
        }
        String token = hashMap.get("access_token");
        /**
         * 获取用户基本信息:GET https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
         */
        Map<String, String> userMap = new HashMap<>();
        userMap.put("access_token", token);
        userMap.put("openid", "oFMh16BDP9yoIk-_B7tuRTHg8vXw");
        String userJson = HttpClientUtil.doGet(CommonContants.WEIXIN_USER_URL, userMap);
        HashMap<String, Object> uMap = JSON.parseObject(userJson, HashMap.class);
        log.info("从微信拉取的用户信息：userJson->{}", userJson);
        //首先判断该用户是否与该应用已经授权
        TbUser user = userMapper.selectOne(new QueryWrapper<TbUser>().eq("source_uid", uMap.get("openid")));
        if (ObjectUtil.isNull(user)) {
            user = new TbUser(uMap);
            user.setSourceCode(authorizationCode);
            userMapper.insert(user);
        }
        UserSecurity security = new UserSecurity(user);
        return security;
    }

    public static void main(String[] args) {
        TbUser user = new TbUser();
       // ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,6,60, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
        List<TbUser> list = new ArrayList<>();
       // list.stream().filter()
    }
}
