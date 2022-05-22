package com.laxi.dao;

import com.laxi.pojo.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketMapper {
    @Insert({
            "INSERT INTO login_ticket (user_id,ticket,STATUS,expired) " +
                    "values (#{userId},#{ticket},#{status},#{expired});"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket ticket);

    @Select({"SELECT id,user_id,ticket,STATUS,expired " +
            " FROM login_ticket " +
            " WHERE ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);


    @Update({"UPDATE login_ticket  " +
            "SET STATUS=#{status} " +
            "WHERE ticket=#{ticket}"})
    int updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
