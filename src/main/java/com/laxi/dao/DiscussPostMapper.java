package com.laxi.dao;

import com.laxi.pojo.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    //动态sql,没有传入userId就查询所有的帖子
    // start 起始行号，length 每页最多显示多少行
    List<DiscussPost> selectDiscussPosts(@Param("userId") Integer userId, @Param("start") int start, @Param("length") int length);

    int selectDiscussPostRows(@Param("userId") Integer userId);
}
