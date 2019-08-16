package com.zj.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("这是一个菜单的实体类。")
@Data
public class Menu extends BaseModel implements Serializable {

    private static final long serialVersionUID = 3831715507716156921L;

    @ApiModelProperty("这是菜单的id的字段。")
    private Long id;

    @ApiModelProperty("这是菜单的名称的字段。")
    private String menuName;

    @ApiModelProperty("这是子菜单的父id的字段。")
    private Long parentId;

    @ApiModelProperty("这是菜单的等级的字段。")
    private Integer leval;

    @ApiModelProperty("这是菜单的url的字段。")
    private String url;

    @ApiModelProperty("这是菜单的子菜单的字段。")
    private List<Menu> menuInfoList;

    @ApiModelProperty("这是最大菜单的名称的字段。")
    private String label;


}
