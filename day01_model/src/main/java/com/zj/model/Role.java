package com.zj.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("这是一个角色的实体类。")
@Data
public class Role extends BaseModel implements Serializable {

    private static final long serialVersionUID = -451410287910357533L;

    @ApiModelProperty("这是角色的id字段。")
    private Long id;

    @ApiModelProperty("这是角色的名称的字段。")
    private String roleName;

    @ApiModelProperty("这是角色的描述的字段。")
    private String miaoShu;

    @ApiModelProperty("这是角色所拥有的用户的字段。")
    private List<User> user;

    private String userName;

    @ApiModelProperty("这是角色的等级的字段。")
    private Integer leval;

}
