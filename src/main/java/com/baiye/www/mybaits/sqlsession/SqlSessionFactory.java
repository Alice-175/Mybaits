package com.baiye.www.mybaits.sqlsession;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/06/11:04
 * @Description:
 */
public interface SqlSessionFactory {
    SqlSession openSession();

    SqlSession openSession(boolean autoCommit);
}
