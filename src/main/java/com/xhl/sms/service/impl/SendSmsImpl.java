package com.xhl.sms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.xhl.sms.service.SendSmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author MuYan
 * @date 2020-05-02 14:37
 */
@Service
public class SendSmsImpl implements SendSmsService {
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.signName}")
    private String signName;
    /**
     * 发送短信
     * @param phoneNum 手机号
     * @param templateCode 阿里云模板code
     * @param code 短信验证码
     * @return
     */
    @Override
    public boolean sendSms(String phoneNum, String templateCode, Map<String, Object> code) {
        //链接阿里云 ,accessKeyId:阿里云的key  AccessKeySecret:创建用户时候要记得保存，否则不见
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        //构建请求
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        //不能动
        request.setDomain("dysmsapi.aliyuncs.com");
        //不能动
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        //自定义参数 手机号，验证码，签名，模板
        request.putQueryParameter("PhoneNumbers", phoneNum);
        //阿里云签名
        request.putQueryParameter("SignName", signName);
        //模板code
        request.putQueryParameter("TemplateCode", templateCode);
        //验证码
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(code));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
