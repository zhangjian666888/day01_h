package com.zj.service;

import com.zj.dao.MenuDao;
import com.zj.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MenuService {

    @Autowired
    private MenuDao menuDao;

    public List<Menu> findAllMenu(){

        List<Menu> list = menuDao.selectMenu(1, 0l);

        this.findOtherMenu(list);

        return  list;

    }

    public void findOtherMenu(List<Menu> menus){

        for (Menu m:menus){

            List<Menu> menus1 = menuDao.selectMenu(m.getLeval() + 1, m.getId());

            m.setMenuInfoList(menus1);

            m.setLabel(m.getMenuName());

            if(menus1.size()>0){

                this.findOtherMenu(menus1);

            }

        }

    }

    /**
     * 添加菜单
     * @param menu
     */
    public void addMenu(Menu menu){

        menuDao.insertMenu(menu);

    }

    /**
     * 修改菜单
     * @param map
     */
    public void updateMenu(Map<String,Object> map){

        menuDao.updateMenu ( map );

    }

    /**
     * 根据用户角色查出菜单不递归
     * @param id
     * @return
     */
    public List<Menu> findMenuByRoleId(Long id){

        List<Menu> menus = menuDao.selectMenus(id);


        return menus;

    }

    /**
     * 根据角色id删除菜单和角色的中间表
     * @param roleId
     */
    public void removeMenuAndRole(Long roleId){

        menuDao.delectMenuAndRole(roleId);

    }

    /**
     * 添加菜单和角色的中间表
     * @param meunId
     * @param roleId
     */
    public void addMenuAndRole(Long []meunId,Long roleId){

        menuDao.insertMenuAndRole(meunId,roleId);

    }

    public void removeMenu(Long id){

        menuDao.delectMenu(id);

    }

    public List<Menu> findAllMenuByRid(Long id){

        List<Menu> list = menuDao.selectMeunByRoleId(id, 1,0l);

        this.getChildrenMenu(list,id);

        return  list;

    }

    /**
     * 获取子权限的递归方法
     * @param firstMenuInfo
     * @param roleId
     */
    public void getChildrenMenu(List<Menu> menus,Long roleId){

        for (Menu m:menus){

            List<Menu> menus1 = menuDao.selectMeunByRoleId(roleId,m.getLeval() + 1, m.getId());

            m.setMenuInfoList(menus1);

            m.setLabel(m.getMenuName());

            if(menus1.size()>0){

                this.getChildrenMenu(menus1,roleId);

            }

        }

    }

}
