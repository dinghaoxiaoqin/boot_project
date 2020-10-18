package com.rrk.oauth.controller;

import com.rrk.common.R;
import com.rrk.common.modules.user.entity.OauthClientDetails;
import com.rrk.oauth.service.impl.MyClientDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ClientController {

    @Autowired
    private MyClientDetailsService myClientDetailsService;

    /**
     * 新增clientDetail
     */
    @PostMapping(value = "/addClient")
    public R<Object> addClient(@RequestBody OauthClientDetails oauthClientDetails){
       Integer count =  myClientDetailsService.addClientDetail(oauthClientDetails);
        if (count > 0) {
            return R.ok(200,"添加成功");
        }
        return  R.fail(430,"添加client失败");
    }
}
