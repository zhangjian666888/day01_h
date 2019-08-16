package com.zj;

import com.zj.model.Menu;
import com.zj.service.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {

    @Autowired
    private MenuService menuService;

    @Test
    public void MyTest(){

        List<Menu> allMenuByRid = menuService.findAllMenuByRid(1L);

        allMenuByRid.forEach(s->{

            System.out.println(s);

        });

    }

}
