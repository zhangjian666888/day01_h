package com.zj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private Environment environment;

    @Bean
    public Docket getDocket(){

        Docket docket=new Docket(DocumentationType.SWAGGER_2);

        docket.apiInfo(setApiInfo());

        return docket;

    }

    public ApiInfo setApiInfo(){

        Contact contact=new Contact("张三","http://loalhost:8080","1152765051@qq.com");

        ApiInfo apiInfo=new ApiInfo(
                "蛟龙系统管理",
                "系统会信息的描述信息",
                "v-1.0",
                "http://loalhost:8080",
                contact,"监听信息",
                "http://www.baidu.com",
                new ArrayList<VendorExtension>()
        );

        return apiInfo;
    }


}
