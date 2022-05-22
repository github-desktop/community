package com.laxi;

import com.laxi.dao.DiscussPostMapper;
import com.laxi.dao.LoginTicketMapper;
import com.laxi.dao.UserMapper;
import com.laxi.pojo.LoginTicket;
import com.laxi.pojo.User;
import com.laxi.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    DiscussPostMapper discussPostMapper;

    @Autowired
    MailClient mailClient;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Test
    void contextLoads() {

    }

    @Test
    void testLoginticket() {
        loginTicketMapper.updateStatus("hehe", 0);
        LoginTicket ticket = loginTicketMapper.selectByTicket("hehe");
        System.out.println(ticket);
    }

    void insertLoginTicket() {
        LoginTicket ticket = new LoginTicket();
        ticket.setTicket("hehe");
        ticket.setStatus(1);
        ticket.setExpired(new Date());
        ticket.setUserId(5);
        int i = loginTicketMapper.insertLoginTicket(ticket);
        System.out.println(i);
    }

    @Test
    void insetTest() {
        User user = new User();
        user.setUsername("测试用户四");
        int i = userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());
    }

    @Test
    void uuid() {
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(s);
        System.out.println(s.length());
    }

    void sendMail() {
        Context context = new Context();
        context.setVariable("username", "王熙来");
        String content = templateEngine.process("/mail/mailtest", context);
        System.out.println(content);
        mailClient.sendMailHtml("949864130@qq.com", "测试邮件", content);
    }

    User getUser() {
        User user = new User();
        user.setActivationCode("激活码");
        user.setCreateTime(new Date());
        user.setEmail("949864130@qq.com");
        user.setHeaderUrl("头像链接");
        user.setUsername("测试用户三");
        user.setPassword("d209b28b19fdcb4aafc3a795157a4651");
        user.setSalt("3aebf");
        user.setStatus(1);
        user.setStatus(1);
        return user;
    }

}
