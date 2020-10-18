package com.rrk.oauth.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.rrk.common.constant.SmsConstant;
import com.rrk.common.dto.SmsResult;
import com.rrk.common.handle.MyException;
import lombok.Data;
import com.aliyuncs.http.MethodType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 对接阿里短信服务
 * @date 2020 -7-25
 * @author dinghao
 * 短信服务工具类
 */
@Data
@Component
@Slf4j
public class SmsUtil {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.regionId}")
    private String regionId;
    @Value("${aliyun.signName}")
    private String signName;
    @Value("${aliyun.templateCode}")
    private String templateCode;

    private final static String OK = "OK";

    /**
     * 发送短信
     */
    public String sendSms(String phoneNumbers) {
        if (StrUtil.isEmpty(phoneNumbers)) {
            return null;
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //下面是常量
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(SmsConstant.SYSDOMAIN);
        request.setSysVersion(SmsConstant.SYSVERSION);
        request.setSysAction(SmsConstant.SYSACTION);
        request.putQueryParameter("RegionId", regionId);
        // 设置手机号
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        // 设置签名模板
        request.putQueryParameter("SignName", signName);
        // 设置短信模板
        request.putQueryParameter("TemplateCode", templateCode);
        // 设置短信验证码（获取短信验证码）
        String smsCode = getCode();
        request.putQueryParameter("TemplateParam", "{\"code\":" + smsCode +"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            //返回的数据
            log.info("返回的数据:"+response.getData());
             SmsResult smsResult = JSON.parseObject(response.getData(), SmsResult.class);
            // 当 message 与 code 均为 ok 时，短信发送成功、否则失败
            if (SmsUtil.OK.equals(smsResult.getMessage()) && SmsUtil.OK.equals(smsResult.getCode())) {
                return smsCode;
            }
            return null;
        } catch (Exception e) {
            throw new MyException("发送短信失败，请联系管理员");
        }
    }

    /**
     * 获取 6 位验证码
     */
    public String getCode() {
        return String.valueOf((int)((Math.random()*9+1)*100000));
    }

}
