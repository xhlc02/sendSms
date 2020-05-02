package com.xhl.sms.controller;

import com.aliyuncs.utils.StringUtils;
import com.xhl.sms.service.SendSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author MuYan
 * @date 2020-05-02 14:40
 */
@RestController
@CrossOrigin
public class SendSmsController {

    @Value("${aliyun.templateCode}")
    private String templateCode;
    @Autowired
    private SendSmsService sendSmsService;

    @Autowired
    private RedisTemplate<String ,String > redisTemplate;
    @GetMapping("/send/{phone}")
    public String code(@PathVariable("phone")String phone){
        //调用方法发送
        String code = redisTemplate.opsForValue().get(phone);
        if(StringUtils.isNotEmpty(code)){
            return phone+":"+code+"已存在，还没有过期";
        }
        //生成验证码并存储到redis中
         code = UUID.randomUUID().toString().substring(0, 4);
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",code);
        boolean isSend = sendSmsService.sendSms(phone, templateCode, map);
        if(isSend){
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.SECONDS);
            return phone+":"+code+"发送成功";
        }else{
            return "发送失败";
        }
    }
}
