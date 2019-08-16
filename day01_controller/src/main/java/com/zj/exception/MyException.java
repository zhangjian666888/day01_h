package com.zj.exception;

import com.zj.common.ResponseResult;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;


public class MyException {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest request,Exception e){

        ResponseResult responseResult = new ResponseResult();

        if(e instanceof ClassCastException){

            e.printStackTrace();

            responseResult.setError("参数类型错误!");

            responseResult.setCode(500);

            return responseResult;

        }else if(e instanceof NullPointerException){

            e.printStackTrace();

            responseResult.setError("参数为空!");

            responseResult.setCode(500);

            return responseResult;

        }else if(e instanceof SQLException){

            e.printStackTrace();

            responseResult.setError("sql语句出错!");

            responseResult.setCode(500);

            return responseResult;

        }else if(e instanceof RuntimeException){

            e.printStackTrace();

            responseResult.setError("您没有权限,不能访问该资源!");

            responseResult.setCode(500);

            return responseResult;

        } else {

            e.printStackTrace();

            responseResult.setError(e.getMessage());

            responseResult.setCode(500);

            return responseResult;

        }

    }

}
