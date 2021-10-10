package com.baiye.www.dao;

import com.baiye.www.domain.User;
import com.baiye.www.mybaits.annotation.Param;
import com.baiye.www.mybaits.annotation.Select;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 *
 * 用户的持久层接口
 */
public interface IUserDao {

    /**
     * 查询所有操作
     * @return
     */
    @Select("select * from user")
    List<User> findAll();
    @Select("select * from user where id=#{id}")
    User findById(String id);
    List<User> findSomeOgnl(User u);
    List<User> findSome(@Param("username") String username,@Param("sex") String sex);
    void insertUser(User user);

    void deletebyId(String s);

    void update(User user);
}
