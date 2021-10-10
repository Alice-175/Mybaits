package com.baiye.www.mybaits.sqlsession.defaults;

import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.sqlsession.SqlSession;
import com.baiye.www.mybaits.sqlsession.SqlSessionFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/07/10:48
 * @Description:
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;
    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration, false);
    }
    @Override
    public SqlSession openSession(boolean autoCommit) {
        return new DefaultSqlSession(configuration, autoCommit);
    }
}
