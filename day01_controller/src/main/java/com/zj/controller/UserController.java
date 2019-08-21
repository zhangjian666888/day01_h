package com.zj.controller;

import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zj.common.ResponseResult;
import com.zj.core.utils.MD5;
import com.zj.core.utils.UID;


import com.zj.model.QueryParam;
import com.zj.model.User;
import com.zj.service.UserService;
import com.zj.yanZhengCode.VerifyCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(tags = "这是一个用户的接口")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    static public String imgUrl;

    @ApiOperation(value = "这是一个用户接口中上传的方法",notes = "注意加跨域注解")
    @ApiImplicitParam(
            name = "files",
            value = "接收要上传的文件。",
            dataType = "file"
    )
    @RequestMapping("/toUpload")
    @CrossOrigin
    public String toUpload(@RequestParam("file") MultipartFile[] files) throws IOException {

        // 上传并且生成缩略图
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                files[0].getInputStream(), files[0].getSize(), "png", null);

        // 获取缩略图路径
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());

        this.imgUrl = path;

        System.out.println(path);

        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());



        return "ok";

    }

    @ApiOperation("这是这个用户接口中修改的方法。")
    @ApiImplicitParam(
            name = "user",
            value = "接收要上传的文件。",
            dataType = "User"
    )
    @RequestMapping("/updateUser")
    public ResponseResult updateUser(@RequestBody User user) {

        ResponseResult responseResult = new ResponseResult();

        if(this.imgUrl!=null){

            user.setPhoto(this.imgUrl);

        }

        userService.changeUser(user);

        responseResult.setCode(200);

        responseResult.setSuccess("恭喜您,修改成功!");

        return responseResult;

    }

    @ApiOperation("这是这个用户接口中删除的方法。")
    @ApiImplicitParam(
            name = "ids",
            value = "接收要删除的用户的id。",
            dataType = "Array"
    )
    @RequestMapping("/delUser")
    public ResponseResult delUser(@RequestBody Long[] ids) {

        ResponseResult responseResult = new ResponseResult();

        userService.removeUser(ids);

        responseResult.setCode(200);

        responseResult.setSuccess("恭喜您,删除成功!");

        return responseResult;

    }

    @ApiOperation("这是这个用户接口中添加的方法。")
    @ApiImplicitParam(
            name = "user",
            value = "接收要添加的用户。",
            dataType = "User"
    )
    @RequestMapping("/addUser")
    public ResponseResult addUser(@RequestBody User user) {

        ResponseResult responseResult = new ResponseResult();

        User userByLoginName = userService.findUserByLoginName(user.getLoginname());

        if(userByLoginName!=null){

            responseResult.setCode(505);

            responseResult.setSuccess("对不起,该用户已注册!");

        }else{

            user.setId(Long.parseLong(UID.getUUIDOrder()));

            user.setPassword(MD5.encryptPassword(user.getPassword(), "zj"));

            user.setPhoto(this.imgUrl);

            userService.addUser(user);

            userService.addUserAndRole(user.getId(),1000000239563434L);

            responseResult.setCode(200);

            responseResult.setSuccess("恭喜您,添加成功!");

        }


        return responseResult;

    }

    @ApiOperation("这是用户接口中的带模糊和分页的查询方法。")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "page",
                    value = "接收当前页数。",
                    dataType = "int",
                    dataTypeClass = Integer.class,
                    defaultValue = "1"
            ),
            @ApiImplicitParam(
                    name = "rows",
                    value = "接收每页显示的条数。",
                    dataType = "int",
                    dataTypeClass = Integer.class,
                    defaultValue = "10"
            ),
            @ApiImplicitParam(
                    name = "username",
                    value = "接收用户名做模糊查询。",
                    dataType = "string",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "startDate",
                    value = "接收查询开始的时间。",
                    dataType = "string",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "endDate",
                    value = "接收查询结束的时间。",
                    dataType = "string",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "sex",
                    value = "接收性别,来根据性别查询。",
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @RequestMapping("/selUser")
    public ResponseResult selUser(@RequestParam(defaultValue = "1", name = "page") Integer page,
                                  @RequestParam(defaultValue = "10", name = "rows") Integer rows,
                                  String username, String startDate, String endDate, Integer sex) {

        ResponseResult responseResult = new ResponseResult();

        PageInfo<User> user = userService.findUser(page, rows, username, startDate, endDate, sex);

        responseResult.setPageInfo(user);

        return responseResult;

    }
/*
    @RequestMapping("/addUserAndRole")
    public ResponseResult addUserAndRole(Long uid, Long rid) {

        ResponseResult responseResult = new ResponseResult();

        userService.addUserAndRole(uid, rid);

        responseResult.setCode(200);

        responseResult.setSuccess("恭喜您,添加成功!");

        return responseResult;

    }*/
/*下午修改*/
    @ApiOperation("这是用户接口中的查询改用户有没有绑定角色。")
    @ApiImplicitParam(
            name = "uid",
            value = "接收用户的id。",
            dataType = "Long",
            dataTypeClass = Long.class
    )
    @RequestMapping("/selUserAndRole")
    public ResponseResult selUserAndRole(Long uid) {

        ResponseResult responseResult = new ResponseResult();

        User userAndRole = userService.findUserAndRole(uid);


        if(userAndRole.getRid()==1000000239563434L){

            responseResult.setCode(200);

            return responseResult;

        }else{

            if (userAndRole != null) {

                responseResult.setCode(500);

                responseResult.setError("该用户已绑定角色!");

                return responseResult;

            } else {

                responseResult.setCode(200);

                return responseResult;

            }

        }

    }

    @ApiOperation("这是用户接口中解除用户绑定角色和绑定角色的方法。")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId",
                    value = "接收用户的id。",
                    dataType = "Long",
                    dataTypeClass = Long.class
            ),
            @ApiImplicitParam(
                    name = "roleId",
                    value = "接收用户要绑定的角色的id。",
                    dataType = "Long",
                    dataTypeClass = Long.class
            ),
            @ApiImplicitParam(
                    name = "rid",
                    value = "接收当前用户绑定的角色的id。",
                    dataType = "Long",
                    dataTypeClass = Long.class
            )

    })
    @RequestMapping("/delUserAndRole")
    public ResponseResult delUserAndRole(Long userId,Long roleId,Long rid){

        ResponseResult responseResult = new ResponseResult();

        if(rid==1000000239563434L){

            userService.updateUserAndRoleTwo(userId,roleId);

            responseResult.setCode(200);

            responseResult.setSuccess("恭喜您,绑定成功!");

        }else{

            userService.updateUserAndRole(userId,roleId);

            responseResult.setCode(200);

            responseResult.setSuccess("恭喜您,解除成功!");

        }

        return responseResult;

    }

    @ApiOperation("这是用户接口中通过邮箱查询用户的方法。")
    @ApiImplicitParam(
            name = "email",
            value = "接收要查询的邮箱。",
            dataType = "String",
            dataTypeClass = String.class
    )
    @RequestMapping("/selUserByEmail")
    public ResponseResult selUserByEmail(String email){

        ResponseResult responseResult = ResponseResult.getResponseResult();

        User userByEmail = userService.findUserByEmail(email);

        if(userByEmail==null){

            responseResult.setCode(200);

        }else{

            responseResult.setCode(500);

            responseResult.setError("您输入的邮箱已被绑定!");

        }

        return responseResult;

    }

    @ApiOperation("这是用户接口中导出数据的方法。")
    @RequestMapping("/exportDate")
    public ResponseResult ExportDate(@RequestBody User[] userList) throws IOException {

        ResponseResult responseResult = ResponseResult.getResponseResult();

        userService.exportUserList(Arrays.asList(userList));

        responseResult.setCode(200);

        responseResult.setSuccess("导出成功!");

        return responseResult;

    }

    @ApiOperation("这是用户接口中通过手机号查询用户。")
    @ApiImplicitParam(
            name = "tel",
            value = "接收要查询的手机号。",
            dataType = "String",
            dataTypeClass = String.class
    )
    @RequestMapping("/selUserByTel")
    public ResponseResult selUserByTel(String tel){

        ResponseResult responseResult = ResponseResult.getResponseResult();

        User userByTel = userService.findUserByTel(tel);

        if(userByTel==null){

            responseResult.setCode(200);

        }else{

            responseResult.setCode(500);

            responseResult.setError("改手机号已被注册,请先登陆!");

        }

        return responseResult;

    }

    @ApiOperation("这是用户接口中通过邮箱发送验证码的方法。")
    @ApiImplicitParam(
            name = "email",
            value = "接收要发送的邮箱。",
            dataType = "String",
            dataTypeClass = String.class
    )
    @RequestMapping("/selEmail")
    public ResponseResult selEmail(String email){

        ResponseResult responseResult = ResponseResult.getResponseResult();

        User userByEmail = userService.findUserByEmail(email);

        if(userByEmail!=null){

            MimeMessage message=mailSender.createMimeMessage();

            try {

                //true表示需要创建一个multipart message
                MimeMessageHelper helper=new MimeMessageHelper(message,true);

                helper.setFrom(from);

                helper.setTo(email);

                helper.setSubject("密码重置");

                helper.setText("<html><head></head><body>请点击这个链接修改密码:<a href='https://localhost:8080/view/shouye/user/updatePassword?ss=2&email="+email+"'>https://localhost:8080/view/shouye/user/updatePassword?ss=2</a>,5分钟后过期。</body></html>",true);

                mailSender.send(message);

                redisTemplate.opsForValue().set(email,email);

                responseResult.setCode(200);

            }catch (Exception e){

                responseResult.setCode(500);

                responseResult.setError("发送失败!");

            }

        }else{

            responseResult.setCode(500);

            responseResult.setError("该邮箱没有绑定用户!");

        }

        return responseResult;
    }

/*    @ApiOperation("这是用户接口中比对邮箱中验证码的方法。")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "code",
                    value = "存储验证码",
                    dataType = "String",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "email",
                    value = "接收邮箱",
                    dataType = "String",
                    dataTypeClass = String.class
            )
    })
    @RequestMapping("/selEmailCode")
    public ResponseResult selEmailCode(String code,String email){

        ResponseResult responseResult = ResponseResult.getResponseResult();

        String s = redisTemplate.opsForValue().get("emailcode" + email);

        if(s!=null){

            if(s.equals(code)){

                redisTemplate.delete("emailcode" + email);

                responseResult.setCode(200);

            }else{


                responseResult.setCode(500);

                responseResult.setError("您输入的验证码有误，请重新输入!");

            }

        }else{

            responseResult.setCode(500);

            responseResult.setError("您的验证码已过期!");

        }

        return responseResult;

    }*/

    @ApiOperation("这是用户接口中通过邮箱验证修改密码的方法。")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "password",
                    value = "接收要修改的密码。",
                    dataType = "String",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "email",
                    value = "接收要邮箱。",
                    dataType = "String",
                    dataTypeClass = String.class
            ),
    })
    @RequestMapping("selUpdatePassword")
    public ResponseResult selUpdatePassword(String password,String email){

        ResponseResult responseResult = ResponseResult.getResponseResult();

        String s = redisTemplate.opsForValue().get(email);

        if(s!=null){

            User userByEmail = userService.findUserByEmail(email);

            String zj = MD5.encryptPassword(password, "zj");

            userByEmail.setPassword(zj);

            userService.changeUser(userByEmail);

            redisTemplate.delete(email);

            responseResult.setCode(200);

        }else{

            responseResult.setCode(500);

            responseResult.setError("你做了违规操作!");

        }

        return responseResult;

    }

}
