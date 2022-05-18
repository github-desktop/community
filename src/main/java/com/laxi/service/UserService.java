package com.laxi.service;

import com.laxi.dao.UserMapper;
import com.laxi.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }
}
