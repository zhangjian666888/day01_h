package com.zj.dao;

import com.zj.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

@Mapper
public interface UserDao {

    public User selectUserByLoginName(@Param("loginName") String loginName);

    public void insertUser(User user);

    public void deleteUser(@Param("ids") Long []ids);

    public void updateUser(User user);

    public List<User> selectUser(@Param("username")String username, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("sex")Integer sex);

    public List<User> selectUserByRoleId(@Param("rid")Long rid);

    public void insertRoleAndUser(@Param("rid")Long rid,@Param("uid")Long uid);

    public User selectRoleAndUser(@Param("uid")Long uid);

    public void updateUserAndRole(@Param("roleId")Long roleId,@Param("userId")Long userId);

    public void updateUserAndRoleTwo(@Param("roleId")Long roleId,@Param("userId")Long userId);

    public User selectUserByTel(@Param("tel")String tel);

    public User selectUserByEmail(@Param("email")String email);

}
