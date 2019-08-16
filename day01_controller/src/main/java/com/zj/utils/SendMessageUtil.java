package com.zj.utils;

import com.aliyun.mns.common.ClientException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class SendMessageUtil {

    public static void main(String[] args) {

        String s = sendSms("15010482273", "2222");

        System.out.println(s);

    }

    /**
     * 发送短信验证码
     * @param phoneNumber
     * @param code
     * @return
     */
    public static String sendSms(String phoneNumber, String code) {

        //设置超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIecjQ4QTVzxUC","38yp1XUWtJkGvYDRiBC5i4geAC3JsV");
        try {
            try {
                DefaultProfile.addEndpoint("cn-hangzhou","cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
            } catch (com.aliyuncs.exceptions.ClientException e) {
                e.printStackTrace();
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient client = new DefaultAcsClient(profile);

        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        // 必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName("ZhangItem");
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_172520233");
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的用户,您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return sendSmsResponse.getCode();
    }

}
