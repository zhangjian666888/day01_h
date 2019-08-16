package com.zj.dao;

import com.zj.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleDao {

    public List<Role> selectRoleByUserId(@Param("uid")Long uid);

    public List<Role> selectRole(@Param("roleName") String roleName);

    public List<Role> selectAllRole();

    public void insertRole(Role role);

    public void deleteRole(@Param("ids") Long []ids);

    public void updateRole(Role role);

}
