package com.laxi;

import com.laxi.dao.DiscussPostMapper;
import com.laxi.dao.UserMapper;
import com.laxi.pojo.DiscussPost;
import com.laxi.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    DiscussPostMapper discussPostMapper;

    @Test
    void contextLoads() {
//        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(null, 1, 10);
//        for (DiscussPost obj : discussPosts) {
//            System.out.println(obj);
//        }
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
