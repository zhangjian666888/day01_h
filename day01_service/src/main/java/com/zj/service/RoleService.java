package com.zj.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zj.dao.RoleDao;
import com.zj.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    /**
     * 根据用户ID查出角色
     * @param uid
     * @return
     */
    public List<Role> findRoleByUid(Long uid){

        List<Role> roles = roleDao.selectRoleByUserId(uid);

        return roles;

    }

    /**
     * 分页查出角色
     * @param page
     * @param rows
     * @param rname
     * @return
     */
    public PageInfo<Role> findRole(Integer page,Integer rows,String rname){

        PageHelper.startPage(page,rows);

        List<Role> roles = roleDao.selectRole(rname);

        return new PageInfo<>(roles);

    }

    /**
     * 添加角色
     * @param role
     */
    public void addRole(Role role){

        roleDao.insertRole(role);

    }

    /**
     * 删除角色
     * @param ids
     */
    public void removeRole(Long []ids){

        roleDao.deleteRole(ids);

    }

    /**
     * 修改角色
     * @param role
     */
    public void changeRole(Role role){

        roleDao.updateRole(role);

    }

    /**
     * 查出所有角色
     * @return
     */
    public List<Role> findAllRole(){

        List<Role> roles = roleDao.selectAllRole();

        return roles;

    }

}
