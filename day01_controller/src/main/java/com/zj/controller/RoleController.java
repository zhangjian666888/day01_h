package com.zj.controller;

import com.github.pagehelper.PageInfo;
import com.zj.core.utils.UID;
import com.zj.model.Role;
import com.zj.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "这是一个角色的接口")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("这是角色接口中修改角色的方法。")
    @ApiImplicitParam(
            name = "role",
            value = "接收一个角色的对象",
            dataType = "Role",
            dataTypeClass = Role.class,
            example = "Role[]"
    )
    @RequestMapping("/updateRole")
    public boolean updateRole(@RequestBody Role role){

        roleService.changeRole(role);

        return true;

    }

    @ApiOperation("这是角色接口中删除角色的方法。")
    @ApiImplicitParam(
            name = "ids",
            value = "接收要删除角色的id",
            dataType = "Array"
    )
    @RequestMapping("/delRole")
    public boolean delRole(@RequestBody Long []ids){

        roleService.removeRole(ids);

        return true;

    }

    @ApiOperation("这是角色接口中添加角色的方法。")
    @ApiImplicitParam(
            name = "role",
            value = "接收一个角色的对象",
            dataType = "Role"
    )
    @RequestMapping("/addRole")
    public boolean addRole(@RequestBody Role role){

        role.setId(Long.parseLong(UID.getUUIDOrder()));

        roleService.addRole(role);

        return true;

    }

    @ApiOperation("这是角色接口中查询角色的方法带分页和模糊。")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "rname",
                    value = "接收模糊查询的角色名",
                    dataType = "String",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "page",
                    value = "接收当前页数",
                    dataType = "int",
                    dataTypeClass = Integer.class,
                    defaultValue = "1"
            ),
            @ApiImplicitParam(
                    name = "rows",
                    value = "接收每页显示的条数",
                    dataType = "int",
                    dataTypeClass = Integer.class,
                    defaultValue = "10"
            )
    })
    @RequestMapping("/selRole")
    public PageInfo<Role> selRole(String rname,
                                  @RequestParam(defaultValue = "1",name = "page")Integer page,
                                  @RequestParam(defaultValue = "10",name = "rows")Integer rows){

        PageInfo<Role> role = roleService.findRole(page, rows, rname);

        return role;

    }

    @ApiOperation("这是角色接口中查询角色的方法。")
    @RequestMapping("/selAllRole")
    public List<Role> selAllRole(){

        List<Role> allRole = roleService.findAllRole();

        return allRole;

    }

}
