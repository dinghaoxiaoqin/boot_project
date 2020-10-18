package com.rrk.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.modules.user.dao.OauthClientDetailsMapper;
import com.rrk.common.modules.user.entity.OauthClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author dinghao
 * 客户端详情信息，客户端详情信息在这里进行初始化，通过数据库来存储调取详情信息
 */
@Service
@Slf4j
public class MyClientDetailsService extends JdbcClientDetailsService {


    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    public MyClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        ClientDetails clientDetails = null;
        try {
            clientDetails = super.loadClientByClientId(clientId);
        } catch (NoSuchClientException e) {
            log.error("clientId:{},{}", clientId, clientId);
        } catch (InvalidClientException e) {
            log.error("cacheAndGetClient-invalidClient:{}", clientId, e);
        }
        return clientDetails;
    }



    public Integer addClientDetail(OauthClientDetails oauthClientDetails) {
         PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //加密
         OauthClientDetails oauthClientDetails1 = oauthClientDetails.setClientSecret(passwordEncoder.encode(oauthClientDetails.getClientSecret()));
       return oauthClientDetailsMapper.insert(oauthClientDetails);
    }

    /**
     * 根据id获取oauth2协议密钥信息
     * @param id
     * @return
     */
    public OauthClientDetails getClientById(Integer id) {
        return oauthClientDetailsMapper.selectOne(new QueryWrapper<OauthClientDetails>().eq("id", id));
    }
}
