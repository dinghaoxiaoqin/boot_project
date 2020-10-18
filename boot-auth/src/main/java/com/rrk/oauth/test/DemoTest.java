package com.rrk.oauth.test;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.rrk.common.modules.commonDto.PermissionCommonDto;
import com.rrk.common.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoTest {

    public static String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp9qYZN8kwjkVsSbXKq+F5299REivjLREVtN5L74TV19X0JF2/H991TnbfTtZ4CcLgA6KKMmaQI8ixPJRN3nGNQ2xp0R7EDjpDH46OIg/cGSWSL2pFc5twGCPoCDYtQtiLLEnzXrhthpM5NYqk1g0jxbf0wFm4WE3m0SZZPUpIK6l1t25o1lq2hDx8irTKcumKmqmCZEYj5S0pcYnY3oD+hJAm45X/U3lvgZoV7xL9UIiD/98to+78jrgv+9UkryYX97HsZ/c1g5sIp/rPT8B2JkBhDTU0+lejEjo7YwuvUmDZ6/ctJaoNm/mk8N+qtEk28qIGsdIwlFAb5DxRuGOPwIDAQAB-----END PUBLIC KEY-----";

    @Test
    public void test01() throws IOException {

        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiZGluZ2hhbyJdLCJjcmVhdGVUaW1lIjoiMjAyMC0wNy0xNCIsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOltdLCJleHAiOjE1OTQ3NjEwMzAsInVzZXJJZCI6MSwiYXV0aG9yaXRpZXMiOlsiL21hbmFnZS9nZXRFbXBsb3llZUxpc3QiLCIvbWFuYWdlL2dldFBhcnRNZW51IiwiL21hbmFnZS9nZXRQYXJ0TGlzdCIsIi9tYW5hZ2UvZ2V0TWVudXMiLCIvbWFuYWdlL2dldFBlcm1pc3Npb25MaXN0IiwiL21hbmFnZS9nZXRQYXJlbnRNZW51cyIsIi9tYW5hZ2UvZGVsRW1wbG95ZWUiLCIvbWFuYWdlL2dldFBhcnRzIiwiL21hbmFnZS9nZXRSb2xlQnlBZG1pbiIsIi9tYW5hZ2UvdXBkYXRlU3RhdHVzIiwiL21hbmFnZS9hZGRQYXJ0UGVybWlzc2lvbiIsIi9tYW5hZ2UvYWRkRW1wbG95ZWUiLCJBRE1JTiIsIi9tYW5hZ2UvYWRkUGVybWlzc2lvbiIsIi9tYW5hZ2UvYWRkUGFydCIsIi9tYW5hZ2UvZGVsUGFydCJdLCJqdGkiOiJmNzFhYWRjOC1jYTcxLTRiYmItODBhOS03MmJjZjkyMjZlNWEiLCJjbGllbnRfaWQiOiJkaW5naGFvIiwidXNlcm5hbWUiOiJhZG1pbiJ9.Eq-HDfSCE4owlZa-co2fV02Lfszz4iMl0DGsVd23KTN-fxIicPTu4GFKz2fRQ9SONNM57iX-hIdOMquushUpDmygRpGpBuIYNmPE03TA8Iq1n7WgJduPcQlkw4j_vA4mtbs_elS3fP9U5eia3bEZ8BC5vxChsKS1PoLALJBzLhB-Wv4VWl5bJ3C3sa4COdgaK-G6gOqmvg12onM44sXDcIcgbbdJksKyJBwl0fMh7OD6Y4qf8Qe1fNdkWc3G-T_gEEESha1NMw-V_enATXDccMPtCAaNSHNquf824VMq-I1cSXUVMf__0vbgq-199ksz8JtZnjQyWX_hDdzKXpCP7g";

        if (StringUtils.isNotEmpty(token)) {
            //Claims claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();
//            Claims claims = Jwts.parser()
//                    .setSigningKey("dev".getBytes(StandardCharsets.UTF_8))
//                    .parseClaimsJws(token)
//                    .getBody();
            // RsaVerifier rsaVerifier = new RsaVerifier(publicKey);
            //Claims claims = Jwts.parser().setSigningKey(new RsaVerifier(publicKey)).parseClaimsJws(token).getBody();
            Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
             String claims = jwt.getClaims();
             Map<String,Object> map = JSON.parseObject(claims, HashMap.class);
            String username = (String) map.get("username");
            Long userId = Convert.toLong(map.get("userId"));
            Object authorities = map.get("authorities");
            List<String> list = JSON.parseArray(authorities.toString(), String.class);
            //  byte[] bytes = claims.getBytes();
//            String username = (String) claims.get("username");

            // 解析对应的角色和权限以及用户信息
            // Object permissionObj = claims.get("authorities");
            // Integer userId = (Integer) claims.get("userId");
            // Set<String> perms = new HashSet<>();
            List<PermissionCommonDto> permissionCommonDtos = new ArrayList<>();
            for (String s2 : list) {
                //  System.out.println("obj:"+object);
                PermissionCommonDto permissionCommonDto = new PermissionCommonDto();
                permissionCommonDto.setPermissionUrl(s2);
                permissionCommonDtos.add(permissionCommonDto);
            }

            System.out.println("获取数据：" + permissionCommonDtos);
        }
    }

    /**
     * 对接第三方微博登录(测试)
     */
    @Test
    public void test06(){

        //1，请求微博第三方服务获取授权码（get请求）主要完成了第三方授权和获取授权码
       // String strGet = "https://api.weibo.com/oauth2/authorize?client_id=1867124886&response_type=code&redirect_uri=http://web.boot.com:6081/webOauth/appLogin";
       String weiGET = "Https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb8fda4063d7cd873&redirect_uri=http://web.boot.com:6081/webOauth/appLogin&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
         //String s = HttpClientUtil.doGet(strGet);
       //通过上面的请求获取的code= 9ce3b10e103f424ef384be8121e9c975
        //2，获取access_token(要用post请求)
        String strPost = "https://api.weibo.com/oauth2/access_token";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","1867124886");
        paramMap.put("client_secret","589d45111a0237eea2345a07f1a5f5ed");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://web.boot.com:6081/webOauth/appLogin");
        paramMap.put("code","7b49a05a4314da6cac0599207d8d0608");

        String access_token = HttpClientUtil.doPost(strPost,paramMap);
        System.out.println("获取的token:"+access_token);
        //3,对token进行解析
        Map<String,String> mapToken = JSON.parseObject(access_token,HashMap.class);
        System.out.println("解析后的token:"+mapToken);

        //4.根据access_token从微博上拉取用户登录信息 https://api.weibo.com/2/users/show.json
        String token = mapToken.get("access_token");
        String s4 = "https://api.weibo.com/2/users/show.json";
        Map<String, String> params = new HashMap<>();
        params.put("uid", mapToken.get("uid"));
        params.put("access_token", mapToken.get("access_token"));
        final String s = HttpClientUtil.doGet(s4,params);
        System.out.println("用户信息："+s);

    }

}
