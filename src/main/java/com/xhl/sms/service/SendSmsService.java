package com.xhl.sms.service;

import java.util.Map;

/**
 * @author MuYan
 * @date 2020-05-02 14:35
 */
public interface SendSmsService {
    /**
     * 发送短信
     * @param phoneNum 手机号
     * @param templateCode 阿里云模板code
     * @param code 短信验证码
     * @return
     */
    public boolean sendSms(String phoneNum,String templateCode, Map<String,Object> code);
}
