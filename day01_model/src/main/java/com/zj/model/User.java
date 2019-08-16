package com.zj.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ApiModel("这是一个用户的实体类。")
@Data
public class User extends BaseModel implements Serializable {

    private static final long serialVersionUID = -1112569710745886286L;

    @ApiModelProperty("这是用户的id字段。")
    private Long id;

    @ApiModelProperty("这是用户的用户名字段。")
    private String username;

    @ApiModelProperty("这是用户的登录名字段。")
    private String loginname;

    @ApiModelProperty("这是用户的密码字段。")
    private String password;

    private String code;

    @ApiModelProperty("这是用户的手机号字段。")
    private String tel;

    /*private int status;*/

    @ApiModelProperty("这是用户的性别字段。")
    private int sex;

    //用户的头像信息
    @ApiModelProperty("这是用户的头像字段。")
    private String photo;

    private Role role;

    private String codekey;

    @ApiModelProperty("这是储存用户权限的字段。")
    private Map<String,String> authmap;

    @ApiModelProperty("这是储存用户菜单的字段。")
    private List<Menu> listMenu;

    private String rname;

    private String rdesc;

    private Long rid;

    private Integer rleval;

    private String key;

    private String email;

    /**
     * 缓存中读取用户数据
     * @param redisTemplate
     * @param userId
     * @return
     */
    public static User getCurrentUser(RedisTemplate redisTemplate, String userId){
        User userinfo = (User)redisTemplate.opsForHash ().get ( userId,"userinfo" );
        return userinfo;
    }

}
