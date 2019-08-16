package com.zj.controller;

import com.zj.common.ResponseResult;
import com.zj.core.CommonUtils;
import com.zj.core.utils.UID;
import com.zj.model.Menu;
import com.zj.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "这是一个菜单的接口")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    /*required=true是否为必填*/
    @ApiOperation("这是菜单接口中的删除菜单的方法:delMeun。")
    @ApiImplicitParam(
            name = "id",
            value = "接收菜单的id",
            dataType = "Long",
            dataTypeClass = Long.class,
            example = "1"
    )
    @RequestMapping("/delMeun")
    public ResponseResult delMeun(Long id){

        ResponseResult responseResult = new ResponseResult();

        menuService.removeMenu(id);

        responseResult.setCode(200);

        responseResult.setSuccess("删除成功!");

        return responseResult;

    }

    @ApiOperation("这是菜单接口中的查询菜单的方法：selAllMeun。")
    @RequestMapping("/selAllMeun")
    public List<Menu> selAllMeun(){

        List<Menu> allMenu = menuService.findAllMenu();

        return allMenu;

    }

    @ApiOperation("这是菜单接口中的添加菜单的方法：addMenu。")
    @RequestMapping("/addMenu")
    public ResponseResult addMenu(HttpServletRequest request){

        ResponseResult responseResult = new ResponseResult();

        Map<String, Object> parameterMap = CommonUtils.getParameterMap(request);

        Menu menu = new Menu();

        if(parameterMap.get("parentId")==null){

            menu.setId(Long.parseLong(UID.getUUIDOrder()));

            menu.setLeval(1);

            menu.setParentId(0l);

            menu.setMenuName(parameterMap.get("menuName").toString());

        }else{

            menu.setId(Long.parseLong(UID.getUUIDOrder()));

            menu.setLeval(Integer.parseInt(parameterMap.get("parentLeval").toString())+1);

            menu.setMenuName(parameterMap.get("currMenuName").toString());

            menu.setUrl(parameterMap.get("url").toString());

            menu.setParentId(Long.parseLong(parameterMap.get("parentId").toString()));

        }

        menuService.addMenu(menu);

        responseResult.setCode(200);

        return responseResult;

    }

    /**
     * 更新菜单
     * @param request
     * @return
     */
    @ApiOperation("这是菜单接口中的更新菜单的方法：updateMenuInfo。")
    @RequestMapping("/updateMenu")
    public ResponseResult updateMenuInfo(HttpServletRequest request){

        ResponseResult responseResult=ResponseResult.getResponseResult ();

        Map<String, Object> parameterMap = CommonUtils.getParamsJsonMap ( request );

        menuService.updateMenu ( parameterMap );

        responseResult.setCode(200);

        return responseResult;
    }

    /**
     * 根据用户角色查出菜单不递归
     * @param id
     * @return
     */
    @ApiOperation("这是菜单接口中的根据用户角色查出菜单不递归的方法：selMenuByRoleId。")
    @ApiImplicitParam(
            name = "id",
            value = "接收角色的id",
            dataType = "Long",
            dataTypeClass = Long.class,
            example = "1"

    )
    @RequestMapping("/selMenuByRoleId")
    public List<Menu> findMenuRoleId(Long id){

        List<Menu> menuByRoleId = menuService.findMenuByRoleId(id);

        return menuByRoleId;

    }

    /**
     * 给角色添加权限
     * @return
     */
    @ApiOperation("这是菜单接口中的给角色添加权限的方法：addMenuAndRole。")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "ids",
                    value = "接收添加所有权限的id的数组",
                    dataType = "Array"

            ),
            @ApiImplicitParam(
                    name = "rid",
                    value = "接收要添加权限的角色的id",
                    dataType = "Long",
                    dataTypeClass = Long.class,
                    example = "1"

            )
    })
    @RequestMapping("/addMenuAndRole")
    public boolean addMenuAndRole(Long []ids,Long rid){

        menuService.removeMenuAndRole(rid);

        if(ids.length > 0){

            menuService.addMenuAndRole(ids,rid);

        }

        return true;

    }

    @ApiOperation("这是菜单接口中的根据角色id查询菜单递归查询的方法：selAllMenuByRoleId。")
    @ApiImplicitParam(
            name = "rid",
            value = "接收角色的id",
            dataType = "Long",
            dataTypeClass = Long.class,
            example = "1"
    )
    @RequestMapping("/selAllMenuByRoleId")
    public List<Menu> selAllMenuByRoleId(Long rid){

        List<Menu> allMenuByRid = menuService.findAllMenuByRid(rid);

        return allMenuByRid;

    }

}
