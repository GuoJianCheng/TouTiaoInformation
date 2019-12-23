package com.gjc;

import com.gjc.dao.CommentDAO;
import com.gjc.dao.LoginTicketDAO;
import com.gjc.dao.NewsDAO;
import com.gjc.dao.UserDAO;
import com.gjc.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
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
    @Resource
    private LoginTicketDAO ticketDAO;
    @Resource
    private CommentDAO commentDAO;

    @Test
    public void contextLoads() {
        Random random = new Random();
        User user = new User();
        News news = new News();
        LoginTicket ticket = new LoginTicket();
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

//        System.out.println(newsDAO.selectByUserIdAndOffset(1,0,2));

//        ticket.setUserId(12);
//        ticket.setTicket("TICKET12");
//        ticket.setExpired(new Date());
//        ticket.setStatus(2);
//        ticketDAO.addTicket(ticket);
//        ticketDAO.updateStatus(ticket.getTicket(),0);

        /*for(int i = 0;i < 3;i++){
            Comment comment = new Comment();
            comment.setUserId(12);
            comment.setEntityId(12);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreateDate(new Date());
            comment.setStatus(0);
            comment.setContent("这是一个评论啊! "+i);
            commentDAO.addComment(comment);
        }*/
        System.out.println(commentDAO.selectByEntity(1,EntityType.ENTITY_NEWS).get(0));
//        System.out.println(commentDAO.getCommentCount(1,EntityType.ENTITY_NEWS));
    }

}
