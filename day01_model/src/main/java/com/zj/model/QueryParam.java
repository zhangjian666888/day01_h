package com.zj.model;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.Date;

@Data
public class QueryParam {

    private String username;

    private String startDate;

    private String endDate;

    private Integer sex;

    private PageInfo pageInfo;

}
