package com.rrk.oauth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.constant.NumConstants;
import com.rrk.common.constant.UserContants;
import com.rrk.common.handle.MyException;
import com.rrk.common.modules.commonConstant.CommonContants;
import com.rrk.common.modules.user.dao.OauthClientDetailsMapper;
import com.rrk.common.modules.user.dao.TbUserMapper;
import com.rrk.common.modules.user.entity.OauthClientDetails;
import com.rrk.common.modules.user.entity.TbUser;
import com.rrk.common.utils.HttpClientUtil;
import com.rrk.common.utils.ToolUtil;
import com.rrk.oauth.annotation.SocialType;
import com.rrk.oauth.config.userConfig.UserSecurity;
import com.rrk.oauth.service.SocialHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dinghao
 * 微博登录的业务处理
 */
@Service
@Transactional(rollbackFor = Exception.class)
@SocialType(socialType = CommonContants.WEIBO_ID)
@Slf4j
public class WeiBoSocialHandleServiceImpl implements SocialHandleService {
    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;
    @Autowired
    private TbUserMapper userMapper;
    @Override
    public UserSecurity getUserSecurity(Integer socialType,String authorizationCode) {
        //用户还没有进行授权
        OauthClientDetails clientDetail = oauthClientDetailsMapper.selectOne(new QueryWrapper<OauthClientDetails>().eq("id", socialType));
        //采用授权码获取access_token
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("client_id", clientDetail.getClientId());
        paramMap.put("client_secret", clientDetail.getResourceIds());
        paramMap.put("grant_type", clientDetail.getAuthorizedGrantTypes());
        paramMap.put("redirect_uri", clientDetail.getWebServerRedirectUri());
        paramMap.put("code", authorizationCode);
        String accessToken = HttpClientUtil.doPost(CommonContants.WEIBO_TOKEN_URL, paramMap);
        log.info("获取第三方的access_token->{}", accessToken);
        //3,对token进行解析
        Map<String, String> mapToken = JSON.parseObject(accessToken, HashMap.class);
        if (ObjectUtil.isNotNull(mapToken.get("error"))) {
            throw new MyException("微博授权登录失败");
        }
        //判断用户是否过去登录过
        TbUser user = userMapper.selectOne(new QueryWrapper<TbUser>().eq("source_uid", mapToken.get("uid")));
        if (ObjectUtil.isNull(user)) {
            Map<String, String> params = new HashMap<>();
            params.put("uid", mapToken.get("uid"));
            params.put("access_token", mapToken.get("access_token"));
            //将用户信息保存到数据库
            String s = HttpClientUtil.doGet(CommonContants.WEIBO_USER_URL, params);
            if (StrUtil.isNotBlank(s)) {
                //解析用户数据
                Map<String, String> userMap = JSON.parseObject(s, HashMap.class);
                user = new TbUser();
                user.setNickName(userMap.get("screen_name"));
                user.setUsername(userMap.get("name"));
                user.setCreateTime(new Date());
                user.setUserRank(UserContants.NOMAL_ROLE);
                user.setEnabled(UserContants.USER_ENABLE);
                user.setUserId(ToolUtil.getLongNum());
                user.setUserSrc(userMap.get("profile_image_url"));
                user.setSourceType(NumConstants.SOURCE_WEIBO);
                user.setSourceUid(mapToken.get("uid"));
                user.setSourceCode(authorizationCode);
                userMapper.insert(user);
            }
        }
        UserSecurity security = new UserSecurity(user);
        return security;
    }
}
