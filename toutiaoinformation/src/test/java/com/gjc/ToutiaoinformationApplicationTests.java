package com.gjc;

import com.gjc.dao.NewsDAO;
import com.gjc.dao.UserDAO;
import com.gjc.model.News;
import com.gjc.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=ToutiaoinformationApplication.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes=ToutiaoinformationApplication.class)
public class ToutiaoinformationApplicationTests {
//这个类必须是public，不能省略不写，要不然Test单元测试没有运行的绿三角图标
//    @Autowired
    @Resource
    private UserDAO userDAO;
    @Resource
    private NewsDAO newsDAO;

    @Test
    public void contextLoads() {
        Random random = new Random();
        User user = new User();
        News news = new News();
//        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
//        user.setName(String.format(String.format("USER%d",11)));
//        user.setPassword("");
//        user.setSalt("");
//        userDAO.addUser(user);

//        user.setId(2);
//        user.setPassword("newpass");
//        userDAO.updatePassword(user);

//        System.out.println(userDAO.selectById(2));

//        userDAO.deleteById(12);

        System.out.println(newsDAO.selectByUserIdAndOffset(1,0,2));
    }

}
