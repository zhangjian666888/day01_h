package com.zj.configure;

import com.zj.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 描述：JWT 配置类
 */

@Configuration
public class JWTConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        //添加自己定义的JwtFilter过滤器
        registrationBean.setFilter(new JwtFilter (redisTemplate));
        //添加拦截路径
        registrationBean.addUrlPatterns("/menu/*","/loginout");
        return registrationBean;
    }
}
