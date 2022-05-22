package com.laxi.service;

import com.laxi.dao.LoginTicketMapper;
import com.laxi.dao.UserMapper;
import com.laxi.pojo.LoginTicket;
import com.laxi.pojo.User;
import com.laxi.util.CommunityConstant;
import com.laxi.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    String path;

    @Autowired
    MailClient mailClient;

    @Autowired
    LoginTicketMapper loginTicketMapper;


    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> hm = new HashMap<>();
        if (username == null || username.equals("")) {
            hm.put("usernameMsg", "账号不能为空");
        }
        if (password == null || password.equals("")) {
            hm.put("passwordMsg", "密码不能为空");
        }
        if (!hm.isEmpty()) return hm;
        User user = userMapper.selectByName(username);
        if (user == null || user.getStatus() == 0) {
            hm.put("usernameMsg", "账号错误，或者未激活");
            return hm;
        }
        password = DigestUtils.md5DigestAsHex((password + user.getSalt()).getBytes());
        if (!password.equals(user.getPassword())) {
            hm.put("passwordMsg", "用户名或密码错误");
            return hm;
        }
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        hm.put("ticket", loginTicket.getTicket());
        return hm;
    }

    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }

    public int activation(Integer userId, String code) {
        User user = userMapper.selectById(userId);
        if (user == null || !user.getActivationCode().equals(code)) return ACTIVATION_FAILURE;

        if (user != null && user.getStatus() == 0 && user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        }
        if (user != null && user.getStatus() == 1) return ACTIVATION_REPEAT;

        return 100;
    }

    //账号 密码 邮箱
    public HashMap<String, Object> register(User user) {

        HashMap<String, Object> error = new HashMap<>();
        if (user == null) {
            error.put("usernameMsg", "请填写正确的信息");
            return error;
        }

        if (user.getUsername() == null) {
            error.put("usernameMsg", "用户名不能为空");
            return error;
        }

        User user1 = userMapper.selectByName(user.getUsername());
        if (user1 != null) {
            System.out.println(user1);
            error.put("usernameMsg", "用户名重复");
            return error;
        }

        if (user.getPassword() == null || user.getPassword() == "") {
            error.put("passwordMsg", "请填写密码");
            return error;
        }

        if (user.getEmail() == null || user.getEmail() == "") {
            error.put("emailMsg", "邮箱填写不正确");
            return error;
        }

        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        user.setSalt(salt);
        String password = DigestUtils.md5DigestAsHex((user.getPassword() + salt).getBytes());
        user.setPassword(password);
        user.setType(0);
        user.setStatus(0);
        user.setCreateTime(new Date());
        user.setActivationCode(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userMapper.insertUser(user);
        //发送邮件将用户id和对应的激活码发给用户
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = path + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMailHtml(user.getEmail(), "牛客社区激活", content);
        return new HashMap<>();
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }
}
