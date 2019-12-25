package com.gjc;

//import com.gjc.dao.CommentDAO;
//import com.gjc.dao.LoginTicketDAO;
//import com.gjc.dao.NewsDAO;
import com.gjc.dao.UserDAO;
import com.gjc.model.*;
import com.gjc.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void testObject() {
        User user = new User();
        user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
        user.setName("user1");
        user.setPassword("123456");
        user.setSalt("salt");

        jedisAdapter.setObject("user1xx",user);

        User u = jedisAdapter.getObject("user1xx",User.class);
        System.out.println(ToStringBuilder.reflectionToString(u));
    }

}
