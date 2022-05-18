package com.laxi.pojo;


import lombok.Data;

import java.util.Date;

@Data
public class DiscussPost {
    Integer id;
    Integer userId;
    String title;
    String content;
    Integer type;
    Integer status;
    Date createTime;
    Integer commentCount;
    Double score;
}
