package com.laxi.service;

import com.laxi.dao.DiscussPostMapper;
import com.laxi.pojo.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(Integer userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostCounts(Integer userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
