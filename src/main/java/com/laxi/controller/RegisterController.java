package com.laxi.controller;

import com.laxi.pojo.User;
import com.laxi.service.UserService;
import com.laxi.util.CommunityConstant;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;

@Controller
public class RegisterController implements CommunityConstant {

    @Autowired
    UserService userService;

    @PostMapping("/register")

    /*
    根据传入的user生成一个包含多数信息的user对象,将user插入到数据库中，发送激活邮件，将http://localhost:8080/community/activation/插入后的userid/code,
    当作激活码的链接，返回一个错误的map，错误的map包括usernameMsg，passwordMsg，emailMsd 返回/site/register
    成功 msg "注册成功" target '/index' 返回/site/operate-result
     */
    public String register(Model model, User user) {
        HashMap<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "请尽快注册");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    @GetMapping("/activation/{userId}/{code}")
    public String activation(@PathVariable("userId") Integer userId, @PathVariable("code") String code, Model model) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功，您的账号已经可以正常使用了");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作");
            model.addAttribute("target", "/index");
        } else if (result == ACTIVATION_FAILURE) {
            model.addAttribute("msg", "激活非法");
            model.addAttribute("target", "/index");
        }
        return "site/operate-result";
    }
}
