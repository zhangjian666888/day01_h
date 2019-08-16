package com.zj.filter;

import com.alibaba.fastjson.JSONObject;
import com.zj.core.utils.JWTUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class MyGlobalFilter implements GlobalFilter {
    //设置不过滤的路径

    @Value("${my.auth.urls}")
    private String[] urls;

    @Value("${my.auth.loginPath}")
    private String loginpage;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求
        ServerHttpRequest request = exchange.getRequest();
        //获取响应
        ServerHttpResponse response = exchange.getResponse();
        //获取当前的请求路径
        String currentpath = request.getURI().toString();

        //验证当前路径是否是公共资源路径也就是不需要进行登录校验的路径
        List<String> strings = Arrays.asList(urls);
        if(strings.contains(currentpath)){
            return chain.filter(exchange);
        }else if(currentpath.indexOf("sel")>0){
            return chain.filter(exchange);
        }else{
            //获取请求头中的token
            List<String> listToken = request.getHeaders().get("token");
            //解密Token校验是否超时，如果超时的话需要重新登录============该步骤是校验Token的
            JSONObject jsonObject=null;
            try {
                //解密判断Token是否已经失效
                jsonObject = JWTUtils.decodeJwtTocken(listToken.get(0));
                //如果不报错说明没有失效,重新加密登录信息
                String token = JWTUtils.generateToken(jsonObject.toJSONString());
                //存储到响应头中
                response.getHeaders().set("token",token);
            }catch (JwtException e){
                e.printStackTrace();
                //表示超时需要重新登录（这种情况一般发生在长时间不登录的情况下使用的旧的Token）
                //或者是错误的Token信息
                //跳转到登录页面
                response.getHeaders().set("Location",loginpage);
                response.setStatusCode(HttpStatus.SEE_OTHER);
                return exchange.getResponse().setComplete();
            }
            //获取用户Id
            String userId = jsonObject.get("id").toString();
            //校验用户有没有访问该资源的权限

            int i = currentpath.indexOf("?");

            if(i > 0){

                currentpath = currentpath.substring(0,i);

            }

            boolean isok = redisTemplate.opsForHash().hasKey("USERDATAAUTH"+userId,currentpath);

            /*=redisTemplate.opsForHash().hasKey("USERDATAAUTH"+userId,currentpath);*/
            //isok=true说明访问资源的权限
            if(isok){
                ///验证当前路径不是需要进行登录校验的路径，直接放过
                return chain.filter(exchange);
            }else{
                throw new RuntimeException("您没有权限,不能访问该资源 !");
            }
        }
    }

}
