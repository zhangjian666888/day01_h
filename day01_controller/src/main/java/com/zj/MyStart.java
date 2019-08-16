package com.zj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "这是控制层的启动类")
@SpringBootApplication
@RestController
public class MyStart {

    public static void main(String[] args) {

        SpringApplication.run(MyStart.class,args);

    }

    @ApiOperation("这是控制层的健康检查方法。")
    @RequestMapping("/health")
    public String health(){

        System.out.println("==========控制层 健康检查开始==========");

        return "ok";

    }

}
