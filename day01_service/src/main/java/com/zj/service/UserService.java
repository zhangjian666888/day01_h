package com.zj.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zj.dao.MenuDao;
import com.zj.dao.RoleDao;
import com.zj.dao.UserDao;
import com.zj.model.Menu;
import com.zj.model.Role;
import com.zj.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    public User findUserByLoginName(String loginName){

        User user = userDao.selectUserByLoginName(loginName);

        if(user!=null){

            List<Role> roles = roleDao.selectRoleByUserId(user.getId());

            user.setRole(roles.get(0));

            if(roles.get(0)!=null){

                //递归的查询子菜单权限
                Map<String,String> authMap=new Hashtable<>();

                List<Menu> firstMenuInfo = menuDao.selectMeunByRoleId(roles.get(0).getId(),1,0L);

                this.getChildrenMenu(firstMenuInfo,authMap,roles.get(0).getId());

                //设置菜单的子权限
                user.setAuthmap(authMap);

                user.setListMenu(firstMenuInfo);

            }

        }

        return user;

    }


    /**
     * 获取子权限的递归方法
     * @param firstMenuInfo
     * @param roleId
     */
    public void getChildrenMenu(List<Menu> firstMenuInfo,Map<String,String> authMap,Long roleId){

        for(Menu m:firstMenuInfo){

            List<Menu> menus1 = menuDao.selectMeunByRoleId(roleId,m.getLeval() + 1, m.getId());

            if(menus1!=null){

                if (m.getLeval()==4){

                    for(Menu menu:firstMenuInfo){

                        if(menu.getUrl()==null){

                            continue;

                        }else{

                            authMap.put(menu.getUrl(),"");
                        }

                    }

                    break;

                }

                m.setMenuInfoList(menus1);

                m.setLabel(m.getMenuName());

                this.getChildrenMenu(menus1,authMap,roleId);

            }else{

                break;

            }

        }

    }

    /**
     * 添加用户
     * @param user
     */
    public void addUser(User user){

        userDao.insertUser(user);

    }

    /**
     * 删除用户
     * @param ids
     */
    public void removeUser(Long []ids){

        userDao.deleteUser(ids);

    }

    /**
     * 修改用户
     * @param user
     */
    public void changeUser(User user){

        userDao.updateUser(user);

    }

    /**
     * 根据条件分页查询用户
     * @param page
     * @param rows
     * @param username
     * @param startDate
     * @param endDate
     * @param sex
     * @return
     */
    public PageInfo<User> findUser(Integer page,Integer rows,String username,String startDate,String endDate,Integer sex){

        PageHelper.startPage(page,rows);

        List<User> users = userDao.selectUser(username, startDate, endDate, sex);

        return new PageInfo<>(users);

    }

    /**
     * 通过角色ID查出用户
     * @param rid
     * @return
     */
    public List<User> findUserByRoleId(Long rid){

        List<User> users = userDao.selectUserByRoleId(rid);

        return users;

    }

    /**
     * 给用户绑定角色
     * @param uid
     * @param rid
     */
    public void addUserAndRole(Long uid,Long rid){

        userDao.insertRoleAndUser(rid,uid);

    }

    /**
     * 通过用户ID和角色ID查询用户是否已绑定角色
     * @param uid
     * @return
     */
    public User findUserAndRole(Long uid){

        User user = userDao.selectRoleAndUser(uid);

        return user;

    }

    /**
     * 解除绑定
     * @param userId
     * @param roleId
     */
    public void updateUserAndRole(Long userId,Long roleId){

        userDao.updateUserAndRole(roleId,userId);

    }

    /**
     * 普通用户的修改
     * @param userId
     * @param roleId
     */
    public void updateUserAndRoleTwo(Long userId,Long roleId){

        userDao.updateUserAndRoleTwo(roleId,userId);

    }

    /**
     * 根据手机号查询用户
     * @param tel
     * @return
     */
    public User findUserByTel(String tel){

        User user = userDao.selectUserByTel(tel);

        return user;

    }

    /**
     * 根据邮箱查找用户
     * @param email
     * @return
     */
    public User findUserByEmail(String email){

        User user = userDao.selectUserByEmail(email);

        return user;

    }

    /*导出数据*/
    private CellStyle getColumnTopStyle(Workbook workbook) {

        CellStyle cellStyle=workbook.createCellStyle();

        cellStyle.setBorderBottom(BorderStyle.THIN);

        cellStyle.setBorderLeft(BorderStyle.THIN);

        cellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.setBorderTop(BorderStyle.THIN);

        //设置自动换行
        cellStyle.setWrapText(false);

        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;

    }

    public void exportUserList(List<User> userList)throws IOException {

        Workbook workbook=new XSSFWorkbook();

        //设置表的牙样式
        CellStyle cellStyle=getColumnTopStyle(workbook);

        Sheet sheet=workbook.createSheet("用户列表");

        int index=0;

        Row row0=sheet.createRow(index++);

        //设置第一行
        row0.createCell(0).setCellValue("编号ID");
        row0.createCell(1).setCellValue("用户名");
        row0.createCell(2).setCellValue("登录名");
        row0.createCell(3).setCellValue("密码");
        row0.createCell(4).setCellValue("性别");
        row0.createCell(5).setCellValue("电话");
        row0.createCell(6).setCellValue("头像路径");
        row0.createCell(7).setCellValue("角色名称");
        row0.createCell(8).setCellValue("创建时间");

        //把查询结果放入到对应的列
        for (User test:userList) {

            Row row=sheet.createRow(index++);

            row.createCell(0).setCellValue(test.getId());
            row.createCell(1).setCellValue(test.getUsername());
            row.createCell(2).setCellValue(test.getLoginname());
            row.createCell(3).setCellValue(test.getPassword());
            row.createCell(4).setCellValue(test.getSex()==1?"男":"女");
            row.createCell(5).setCellValue(test.getTel());
            row.createCell(6).setCellValue(test.getPhoto());
            row.createCell(7).setCellValue(test.getRname());
            row.createCell(8).setCellValue(test.getCreateTime());

        }

        for(int m=0;m<=sheet.getLastRowNum();m++){

            Row rowStyle=sheet.getRow(m);

            for(int n=0;n<rowStyle.getLastCellNum();n++){

                rowStyle.getCell(n).setCellStyle(cellStyle);

            }
        }

        try {

            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\user.xlsx");

            try {

                workbook.write(fileOutputStream);

            } catch (IOException e) {

                e.printStackTrace();

            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }


    }



}
