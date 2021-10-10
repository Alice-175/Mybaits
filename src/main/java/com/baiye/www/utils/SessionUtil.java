package com.baiye.www.utils;

import com.baiye.www.mybaits.io.Resources;
import com.baiye.www.mybaits.sqlsession.SqlSession;
import com.baiye.www.mybaits.sqlsession.SqlSessionFactory;
import com.baiye.www.mybaits.sqlsession.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 双重校验锁法是线程安全的，并且，这种方法实现了lazyloading。
 *
 * @author baiye
 * @version 1.0
 * @date 2021/8/8 8:19
 */
public class SessionUtil {
    public static SqlSession sqlSession = null;

    public static SqlSession getSession() throws IOException {
        if (sqlSession == null) {
            synchronized (SessionUtil.class) {
                if (sqlSession == null) {
                    //1.读取配置文件
                    InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
                    //2.创建SqlSessionFactory工厂
                    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
                    SqlSessionFactory factory;
                    factory = builder.build(in);
                    //3.使用工厂生产SqlSession对象
                    SqlSession session = factory.openSession();
                    sqlSession = session;
                    in.close();
                }
            }
        }
        return sqlSession;

    }
}
