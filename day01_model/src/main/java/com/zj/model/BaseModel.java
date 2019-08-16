package com.zj.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

@ApiModel("这是一个实体类的基类。")
@Data
public class BaseModel {

    @ApiModelProperty("这是一个版本号的字段。")
    private Integer version;

    @ApiModelProperty("这是一个创建的时间字段。")
    private Date createTime;

    @ApiModelProperty("这是一个修改的时间字段。")
    private Date updateTime;

}
