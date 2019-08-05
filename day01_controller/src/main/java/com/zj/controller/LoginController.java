package com.zj.controller;

import com.alibaba.fastjson.JSON;
import com.zj.common.ResponseResult;
import com.zj.core.CommonUtils;
import com.zj.core.utils.AESUtils;
import com.zj.oauthLogin.User;
import com.zj.yanZhengCode.VerifyCodeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@CrossOrigin
public class LoginController {

    /**
     * 获取滑动验证的验证码
     * @return
     */
    @RequestMapping("/getCode")
    public ResponseResult getCode(HttpServletRequest request, HttpServletResponse response){

        String code= VerifyCodeUtils.generateVerifyCode (5);

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        responseResult.setResult ( code );

        return responseResult;
    }

    @RequestMapping("/login")
    public ResponseResult login(HttpServletRequest request) throws Exception {

        ResponseResult responseResult = ResponseResult.getResponseResult ();

        //获取参数
        Map<String, Object> parameterMap = CommonUtils.getParameterMap ( request );

        String canshu=parameterMap.get ( "canshu" ).toString ();

        //AES解密参数
        String decrypt = AESUtils.desEncrypt ( canshu );//json的参数

        User user1 = null;

        User user2 = null;

        if(decrypt!=null){

            user1 = JSON.parseObject(decrypt,User.class);

            String password = user1.getPassword();

        }

        return responseResult;

    }


}
