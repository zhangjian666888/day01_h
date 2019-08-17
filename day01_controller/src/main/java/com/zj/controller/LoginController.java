package com.zj.controller;

import com.alibaba.fastjson.JSON;
import com.zj.common.ResponseResult;
import com.zj.core.utils.JWTUtils;
import com.zj.core.utils.MD5;
import com.zj.core.utils.UID;
import com.zj.exception.LoginException;
import com.zj.model.User;
import com.zj.service.UserService;
import com.zj.utils.SendMessageUtil;
import com.zj.yanZhengCode.VerifyCodeUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Api(tags = "这是一个登陆的接口")
@RestController
public class LoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 获取滑动验证的验证码
     * @return
     */
    @ApiOperation("这是登陆接口中的获取验证码的方法。")
    @RequestMapping("/getCode")
    public ResponseResult getCode(HttpServletRequest request, HttpServletResponse response){

        String code= VerifyCodeUtils.generateVerifyCode (5);

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        responseResult.setResult ( code );

        String uidCode="CODE"+ UID.getUUID16();

        //将生成的随机字符串标识后存入redis
        redisTemplate.opsForValue().set(uidCode,code);

        //设置过期时间
        redisTemplate.expire(uidCode,1, TimeUnit.MINUTES);

        //回写cookie
        Cookie cookie=new Cookie("authcode",uidCode);

        cookie.setPath("/");

        cookie.setDomain("localhost");

        response.addCookie(cookie);

        return responseResult;
    }

    @ApiOperation("这是登陆接口中登陆的方法。")
    @RequestMapping("/login")
    public ResponseResult login(@RequestBody User user) throws Exception {

        ResponseResult responseResult = ResponseResult.getResponseResult ();

        User user2 = null;

        if(user!=null){

            if(user.getKey()==null){

                String code = redisTemplate.opsForValue().get(user.getCodekey());

                //获取传入的验证码是否是生成后存在redis中的验证码
                if(code==null||!code.equals(user.getCode())){

                    responseResult.setCode(500);

                    responseResult.setError("验证码错误,请重新刷新页面登陆");

                    return responseResult;
                }

            }

            if(user.getLoginname()!=null&&user.getLoginname()!=""){

                User userByLoginName = userService.findUserByLoginName(user.getLoginname());

                if(userByLoginName!=null){

                    System.out.println(userByLoginName);

                    String password = MD5.encryptPassword(user.getPassword(), "zj");

                    if(userByLoginName.getPassword().equals(password)){

                        userByLoginName.setListMenu(null);

                        ResponseResult responseResult1 = this.loginCommen(userByLoginName);

                        return responseResult1;

                    }else{
                        throw new LoginException("用户名或密码错误");
                    }

                }else{
                    throw new LoginException("用户名或密码错误");
                }

            }else{
                throw new LoginException("用户名或密码错误");
            }

        }

        return responseResult;

    }

    /**
     * 两种登陆的公共代码
     * @param userByLoginName
     * @return
     */
    @ApiOperation("这是两种登陆的公共代码。")
    public ResponseResult loginCommen(User userByLoginName){

        ResponseResult responseResult = ResponseResult.getResponseResult();

        //将用户信息转存为JSON串
        String userinfo = JSON.toJSONString(userByLoginName);

        //将用户信息使用JWt进行加密，将加密信息作为票据
        String token = JWTUtils.generateToken(userinfo);

        //将加密信息存入statuInfo
        responseResult.setToken(token);

        //将生成的token存储到redis库
        redisTemplate.opsForValue().set("USERINFO"+userByLoginName.getId().toString(),token);

        //将该用户的数据访问权限信息存入缓存中
        redisTemplate.opsForHash().putAll("USERDATAAUTH"+userByLoginName.getId().toString(),userByLoginName.getAuthmap());

        //设置token过期 30分钟
        redisTemplate.expire("USERINFO"+userByLoginName.getId().toString(),7,TimeUnit.DAYS);

        //设置返回值
        responseResult.setResult(userByLoginName);

        responseResult.setCode(200);

        //设置成功信息
        responseResult.setSuccess("登陆成功！^_^");

        //开始记录登陆人数
        Date d = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String nowDate = sdf.format(d);

        if(!redisTemplate.hasKey(nowDate+userByLoginName.getId())){

            redisTemplate.opsForValue().set(nowDate+userByLoginName.getId(),nowDate);

            redisTemplate.expire(nowDate+userByLoginName.getId(),1, TimeUnit.DAYS);

            if(!redisTemplate.hasKey(nowDate)){

                redisTemplate.opsForValue().set(nowDate,"1");

                redisTemplate.expire(nowDate,30, TimeUnit.DAYS);

            }else{

                redisTemplate.opsForValue().increment(nowDate, 1);

            }

        }

        Set<String> keys = redisTemplate.keys("????-??-??");

        String[] objects1 ={} ;

        String[] strings = keys.toArray(objects1);

        List<String> objects = Arrays.asList(strings);

        Collections.sort(objects);

        List<String> values = redisTemplate.opsForValue().multiGet(objects);
        //结束

        responseResult.setKeys(objects);

        responseResult.setValues(values);

        return responseResult;

    }


    @ApiOperation("这是登陆接口中退出的方法。")
    @ApiImplicitParam(
            name = "user",
            value = "user对象",
            dataType = "User",
            required=true
    )
    @RequestMapping("/loginout")
    public ResponseResult loginout(@RequestBody User user){

        ResponseResult responseResult = ResponseResult.getResponseResult ();

        if(user!=null){

            redisTemplate.delete("USERINFO"+user.getId().toString());

            redisTemplate.delete("USERDATAAUTH"+user.getId().toString());

            responseResult.setSuccess ( "ok" );

        }


        return responseResult;
    }

    @ApiOperation("这是通过手机号登陆时，发送验证码的方法。")
    @ApiImplicitParam(
            name = "myPhone",
            value = "接收要给发送验证码的手机号。",
            dataType = "String",
            dataTypeClass = String.class

    )
    @RequestMapping("/selMessage")
    public ResponseResult sendMessage(String myPhone){

        ResponseResult responseResult = ResponseResult.getResponseResult();

        String s = VerifyCodeUtils.generateVerifyCode(4);

        String s1 = SendMessageUtil.sendSms(myPhone, s);

        if(s1.equals("OK")){

            redisTemplate.opsForValue().set("phoneCode",s,60,TimeUnit.SECONDS);

            responseResult.setCode(200);

        }else{

            responseResult.setError("发送失败!");

            responseResult.setCode(500);

        }

        return responseResult;

    }

    /**
     * 手机验证码登陆
     * @param tel
     * @return
     */
    @ApiOperation("通过手机号接收验证码来登陆的方法。")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "code",
                    value = "接收手机收到的验证码。",
                    dataType = "String",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "tel",
                    value = "接收要登录的手机号。",
                    dataType = "String",
                    dataTypeClass = String.class
            )
    })
    @RequestMapping("/selUserByCode")
    public ResponseResult selUserByCode(String code,String tel){

        ResponseResult responseResult = ResponseResult.getResponseResult();

        User userByTel1 = userService.findUserByTel(tel);

        if(userByTel1!=null){

            String phoneCode = redisTemplate.opsForValue().get("phoneCode");

            if(phoneCode==null){

                responseResult.setCode(500);

                responseResult.setError("您的验证码已过期,请重新发送!");

            }else{

                if(phoneCode.equals(code)){

                    String str = userByTel1.getLoginname();

                    User userByLoginName = userService.findUserByLoginName(str);

                    ResponseResult responseResult1 = this.loginCommen(userByLoginName);

                    return responseResult1;

                }else{

                    responseResult.setCode(500);

                    responseResult.setError("您的验证码输入错误,请重新输入!");

                }

            }

        }else{

            responseResult.setCode(500);

            responseResult.setError("您所输入的手机号没有注册,请先注册!");

        }


        return responseResult;

    }

}
