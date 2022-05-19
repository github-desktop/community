package com.laxi.controller;

import com.laxi.pojo.DiscussPost;
import com.laxi.pojo.Page;
import com.laxi.pojo.User;
import com.laxi.service.DiscussPostService;
import com.laxi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @GetMapping("site/register")
    public String register() {
        return "site/register";
    }

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page) {
        page.setRows(discussPostService.findDiscussPostCounts(null));
        page.setPath("/index");
        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(null, page.getOffset(), page.getLimit());
        List<Map<String, Object>> res = new ArrayList<>();
        if (discussPosts != null) {
            for (DiscussPost discussPost : discussPosts) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                User user = userService.findUserById(discussPost.getUserId());
                map.put("user", user);
                res.add(map);
            }
        }
        model.addAttribute("discussPosts", res);
        return "index";
    }
}
