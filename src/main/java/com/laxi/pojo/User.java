package com.laxi.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class User {
    Integer id;
    String username;
    String password;
    String salt;
    String email;
    Integer type;
    Integer status;
    String activationCode;
    String headerUrl;
    Date createTime;
}
