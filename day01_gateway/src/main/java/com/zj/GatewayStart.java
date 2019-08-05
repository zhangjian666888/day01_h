package com.zj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GatewayStart {

    public static void main(String[] args) {

        SpringApplication.run(GatewayStart.class,args);

    }

    @RequestMapping("/health")
    public String health(){

        System.out.println("==========网关 健康检查开始==========");

        return "ok";

    }

}
