package com.zj.dao;

import com.zj.model.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MenuDao {

    /**
     * 根据角色id查询出菜单
     * @param roleId
     * @param leval
     * @return
     */
    public List<Menu> selectMeunByRoleId(@Param("roleId") Long roleId,@Param("leval") Integer leval,@Param("parentId")Long parentId);

    /**
     * 查询出所有的菜单
     * @return
     */
    public List<Menu> selectMenu(@Param("leval") Integer leval,@Param("parentId")Long parentId);

    /**
     * 添加菜单
     * @param menu
     */
    public void insertMenu(Menu menu);

    /**
     * 修改菜单
     * @param map
     * @return
     */
    public void  updateMenu(Map<String,Object> map);

    /**
     * 根据角色id查询出菜单不递归
     * @param roleId
     * @return
     */
    public List<Menu> selectMenus(@Param("roleId") Long roleId);

    /**
     * 根据角色id删除菜单和角色的中间表
     * @param roleId
     */
    public void delectMenuAndRole(@Param("roleId") Long roleId);

    /**
     * 添加菜单和角色的中间表
     * @param meunId
     * @param roleId
     */
    public void insertMenuAndRole(@Param("meunId")Long []meunId,@Param("roleId") Long roleId);

    public void delectMenu(@Param("id")Long id);
}
