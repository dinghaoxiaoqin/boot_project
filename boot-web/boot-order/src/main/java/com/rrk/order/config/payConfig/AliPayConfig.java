package com.rrk.order.config.payConfig;

import com.alipay.api.DefaultAlipayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author dinghao
 * @date 2020-09-14
 * 支付宝支付的相关配置
 */

@Slf4j
@Configuration
public class AliPayConfig {


    public static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCmoYBLrIgRwGkGTK99CciTfDTwMOqGAI3atxjE6CsW+z4OgoKXXpnbDZ68mlH1MxwO1OZfe2ZAMcibEEQx0ofSLYpkSm/y/6rpSbfas58FijH+lXlgThlttmuDSYm0fPxGF0jTJ3GQvZZ0L1NkoEUo8vs+1HWYPajunb+rrbpHZzHaUv7TGj83vJ+lkPB18LMMWJqcVU/w0tGpmd/4/4SjVcJolb6saKakMnlItUW0h5CtinM84P8j8jAjtKNFDrfR8A/QHlGspKgDJaiRoop0K8xF/6+ouO+ytgyb94Z4d5gbI4Dz717o7XGDfluYEYHfj1qE7Yh0BCUFq/0FFjn7AgMBAAECggEAG20rs8jzIJDd+IZ4wAjzNatU/0/d2i6PKrfGYOqh5Qd3gGXk3stYYaDNRtfTs5s0/A/41zB1dQ18qmE0CEtRxbtvMvC5mnKQjBNxKpM0EnJbpj6uGrbgIpnK+nIJM2b7EwJUH0U3wQ4jjH4ZqM9kcsgDqKTJskQ74QKgAL74kTXcFe1kVd0VvAwRB2YAsoBcGS4W3RBdlGptxoK903m7qLFmm8E1HkJn/P5yx4nZ+JWBNxiIalnDKga3kECU9Jk1cYGA3l+6XAJ8eeqUQWAIR1zr393sukPngyZTc9Ze9Z4iO/ocZ49IT+rJptLKSAVusQkEk29RWk52pPtbC3+iqQKBgQDPsVWJIhPPFl4oRBqU512G5Dc7A/1peNIxqXE+sL1L0mmX56BFzQLAXDmOKYYFT2nY+WJ8+uVjjW4ZWAcDWc1hQzxlFQzzagUontHDRs8OEL9KbpArofiq4yizkn3XIZi6YjxWQ5kwNiOM+WAADW3BIOCNsDxoZgBVx8T80mT/pQKBgQDNYze9bZd/yl8IutZaN0lHmijTBnSmBGDtD4x2s5APgwgjpDSmhb+yUltRXFn1IS7baBOQVJWwbg4mPv1oW4FFmHsUjktsdVeyK15z1FL1yxTn3yEvZICTn9ST6bO3E4o3c4ySVeBtCdAAjYaKLhEeNxVhVi18wwyLTzoWOZshHwKBgFkROpZRl98iP4TTF/ctdU1ox0q2vlEh8Do96QOydatzc7ciLPhiH8Dwi/osUn4qfUEASb1BN2hG+aVCu9czBsGkPN04dtJYcBfobBprXlYw1mgMHCWqE2LtgcHom7DLhpy/jhKMcMBdJ0fIlePP3naJK+N3ZaB7xb8DqbWmYIvFAoGBAKViE2wxZyC7sDKzYA9dHWDOvbjPJfbY58N8Fbsvc/1JobAxdUPxxx6sOmQVXUqJ4dVCxR4fPjnHJRWe+yahKEMGOXWf7xPymR8a1P+oJPVPBhKAFZS39ODqfp5sPQ5aaZ2lKbhb9k9W62rGi2ORHK+b41dxsUcZOGyk6z0iPcw5AoGAd4geBXrLx474n2HAmYBwQM0jvA+nIHmlopxpIA2olNnZzJqL4vYwgjGArW04H1LNhG/EUE2SVUbI1BBI6h5eoHCX9iPPWV3DaqC1ZX3YFOih1Kjtv+KYpmVjfn/S4EaNwdjR6T/YFN72NNFjE9xa/e/lLf3oY8OyO9ExM0JOX7k=";

    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApqGAS6yIEcBpBkyvfQnIk3w08DDqhgCN2rcYxOgrFvs+DoKCl16Z2w2evJpR9TMcDtTmX3tmQDHImxBEMdKH0i2KZEpv8v+q6Um32rOfBYox/pV5YE4ZbbZrg0mJtHz8RhdI0ydxkL2WdC9TZKBFKPL7PtR1mD2o7p2/q626R2cx2lL+0xo/N7yfpZDwdfCzDFianFVP8NLRqZnf+P+Eo1XCaJW+rGimpDJ5SLVFtIeQrYpzPOD/I/IwI7SjRQ630fAP0B5RrKSoAyWokaKKdCvMRf+vqLjvsrYMm/eGeHeYGyOA8+9e6O1xg35bmBGB349ahO2IdAQlBav9BRY5+wIDAQAB";

    public static final String NotifyUrl = "http://dhqxq.free.idcfengye.com/order/payCallback.htm";
    /**
     * 商户id
     */

    public static  final  String APP_ID = "2016092600598131";

    /**
     * 请求网关的地址
     */

    public static  final  String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    /**
     * 编码方式
     */

    public static final String CHARSET = "UTF-8";

    public static final String FORMAT = "JSON";
    /**
     * RSA2加密方式
     */

    public static final String SIGN_TYPE = "RSA2";

    /**
     *    实体物品，0：虚拟物品
     */
    public  String GOODSTYPE = "1";

    public static DefaultAlipayClient alipayClient = null;





    //因为支付宝alipayClient本身是线程安全的，因此只用创建一个，创建成单例的模式
    public static DefaultAlipayClient getAlipayClient() {
        if (alipayClient == null) {
            synchronized (AliPayConfig.class) {
                //AlipayConstants.CHARSET_UTF8
                if (null == alipayClient) {
                    alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID,
                            PRIVATE_KEY, FORMAT, CHARSET,
                            PUBLIC_KEY,SIGN_TYPE);
                }
            }
        }
        return alipayClient;
    }

    public static DefaultAlipayClient getDefaultAlipayClient(){
        log.info("获取的支付宝配置信息："+getDefaultAlipayClient());
      return   getDefaultAlipayClient();

    }


}
