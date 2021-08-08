package com.baiye.test;

import com.baiye.www.dao.IUserDao;
import com.baiye.www.domain.User;
import com.baiye.www.mybaits.io.Resources;
import com.baiye.www.mybaits.sqlsession.SqlSession;
import com.baiye.www.mybaits.sqlsession.SqlSessionFactory;
import com.baiye.www.mybaits.sqlsession.SqlSessionFactoryBuilder;
import com.baiye.www.utils.SessionUtil;


import java.io.InputStream;


public class MybatisTest {

    /**
     * 入门案例
     * @param args
     */
    public static void main(String[] args)throws Exception {
        SqlSession session = SessionUtil.getSession();
        //4.使用SqlSession创建Dao接口的代理对象
        IUserDao userDao = session.getMapper(IUserDao.class);
        //5.使用代理对象执行方法
 //       List<User> users = userDao.findAll();
        User user = userDao.findById("41");
        //userDao.insertUser(new User(1,"小花",new Date(1920,11,23),"男","江西"));
//        userDao.deletebyId("49");
 //       userDao.update(new User(42,"小花",new Date(1920,11,23),"男","江西"));
        System.out.println(user);
//        for(User u : users){
//            System.out.println(u);
//        }
        //6.释放资源
        session.close();
    }
}
