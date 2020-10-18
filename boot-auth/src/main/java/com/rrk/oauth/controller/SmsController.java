package com.rrk.oauth.controller;

import cn.hutool.core.util.StrUtil;
import com.rrk.common.R;
import com.rrk.common.constant.NumConstants;
import com.rrk.oauth.util.RedisUtil;
import com.rrk.oauth.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 发送短信获取短信验证码的前端控制器
 */
@RestController
@RequestMapping("/oauth")
@CrossOrigin
@Slf4j
public class SmsController {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 发送短信，手机将获取短信验证码
     */
    @PostMapping("/send")
    public R<Object> send(@RequestParam(value = "phoneNumber") String phoneNumber) {
        //防止重复获取验证码（设置期限为 60s）
        Long expire = redisUtil.getExpire(phoneNumber);
        if (expire != null && (NumConstants.SMS_EXPIRE * 60L - expire < 60L)) {
            return R.fail(437, "验证码已发送，60s后可再次获取验证码");
        }
        String smsCode = smsUtil.sendSms(phoneNumber);
        if (StrUtil.isNotBlank(smsCode)) {
            //将短信存放到redis
            redisUtil.smsCodeCache(phoneNumber, smsCode);
            return R.ok(200, "短信发送成功");
        }
        return R.fail(437, "短信发送失败");
    }

}
