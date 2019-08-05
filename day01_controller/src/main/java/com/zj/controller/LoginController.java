package com.zj.controller;

import com.zj.common.ResponseResult;
import com.zj.yanZhengCode.VerifyCodeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


}
