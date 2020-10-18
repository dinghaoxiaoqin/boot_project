package com.rrk.oauth.config.auth;

import com.rrk.common.modules.commonConstant.CommonContants;
import com.rrk.oauth.config.userConfig.MyUserDetailService;
import com.rrk.oauth.service.impl.MyClientDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;

/**
 * @author dinghao
 * @date 2020-5-12
 * 配置授权服务器
 */
@Configuration
@EnableAuthorizationServer
@AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
@Slf4j
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 注入authenticationManager 来支持 password grant type
     */
    @Autowired
    private MyAuthenticationManager authenticationManager;
    /**
     * 数据源，用于从数据库获取数据进行认证操作
     */
    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyClientDetailsService myClientDetailsService;
    /**
     * jwt令牌转换器
     */
    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private TokenEnhancer MyTokenEnhancer;

    @Bean
    public TokenEnhancer MyTokenEnhancer() {
        return new MyTokenEnhancer();
    }

    /**
     * SpringSecurity
     */
    @Resource
    private MyUserDetailService userDetailsService;

    /**
     * 这个表示可以随机生成一个授权码
     */
   // @Resource
   // private RandomValueAuthorizationCodeServices authorizationCodeServices;

    /**
     * 令牌持久化存储接口
     */
    @Autowired
    TokenStore tokenStore;

    /**
     * 读取密钥的配置
     *
     * @return
     */
    @Bean("keyProp")
    public KeyProperties keyProperties() {
        KeyProperties keyProperties = new KeyProperties();
        KeyProperties.KeyStore keyStore = keyProperties.getKeyStore();
        keyStore.setPassword(CommonContants.GRANT_TYPE_PASSWORD);
        keyStore.setSecret(CommonContants.SECRET);
        keyStore.setLocation(new ClassPathResource(CommonContants.LOCATION));
        keyStore.setAlias(CommonContants.ALIAS);
        return keyProperties;
    }

    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /***
     * 客户端信息配置(这里就是从数据库获取 客户端的clientId 和 客户端 的 clientSecret等等信息)
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(myClientDetailsService);
    }

    /***
     * 授权服务器端点配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        if (jwtAccessTokenConverter != null) {
            if (MyTokenEnhancer != null) {
                TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
                tokenEnhancerChain.setTokenEnhancers(
                        Arrays.asList(MyTokenEnhancer, jwtAccessTokenConverter));
                endpoints.tokenEnhancer(tokenEnhancerChain);
            } else {
                endpoints.accessTokenConverter(jwtAccessTokenConverter);
            }
        }
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                //授权码模式（这里是随机获取授权码）
               // .authorizationCodeServices(authorizationCodeServices)
                //自动刷新token
                .reuseRefreshTokens(true);
    }

    /***
     * 客户端的安全配置
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients()
                .passwordEncoder(new BCryptPasswordEncoder())
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 客户端配置
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Bean
    @Autowired
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }


    /****
     * JWT令牌转换器
     * @param
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //签名的方式改用非对称加密
        System.out.println("获取的公钥数据" + keyProperties.getKeyStore().getLocation());
        KeyPair keyPair = new KeyStoreKeyFactory(
                //证书路径 dinghao.jks
                keyProperties.getKeyStore().getLocation(),
                //证书秘钥 dinghao
                keyProperties.getKeyStore().getSecret().toCharArray())
                //证书别名 dinghao
                .getKeyPair(
                        keyProperties.getKeyStore().getAlias(),
                        //证书密码 dinghao
                        keyProperties.getKeyStore().getPassword().toCharArray());
        converter.setKeyPair(keyPair);
        return converter;
    }

}

