package com.rrk.manage.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 对接抖音的工具类
 */
public class DouyinUtil {

    /**
     * 获取 抖音access_token
     * @param douyinUrl
     * @param key
     * @param secrent
     * @return
     */
    public static String  getAccessToken(String douyinUrl,String key,String secrent){
        String url = douyinUrl + "/oauth2/access_token" + "?app_id=" + key + "&app_secret="
                + secrent + "&grant_type=authorization_self";
        String response = HttpUtil.get(url, 20000);
        if (StrUtil.isBlank(response)) {
            for (int i = 0; i < 3; i++) {
                response = HttpUtil.get(url, 20000);
                if (StrUtil.isNotBlank(response)) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject.getIntValue("err_no") == 0) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (null != data) {
                //用于调用API的access_token
                String accessToken = data.getString("access_token");
                if (StrUtil.isNotBlank(accessToken)) {
                    return accessToken;
                }
            }
        }
        return null;
    }

    /**
     * 获取抖音的sign（签名）
     * @param methodName
     * @param paramJson
     * @param timeStamp
     * @return
     */
    public static String fetchSign(String methodName, String paramJson, String timeStamp,String secrent,String key) {
        String requestStr = secrent + "app_key" + key + "method" + methodName + "param_json" + paramJson + "timestamp"
                + timeStamp + "v2" + secrent;
        return paramToMD5(requestStr);
    }

    /**
     * md5加密算法
     * @param requestStr
     * @return
     */
    public static String paramToMD5(String requestStr) {
        byte[] secretBytes;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    requestStr.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        while (md5code.length() < 32) {
            md5code.insert(0, "0");
        }
        return md5code.toString();
    }
}
