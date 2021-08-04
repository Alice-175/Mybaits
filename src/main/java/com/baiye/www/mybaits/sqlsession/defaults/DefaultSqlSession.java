package com.baiye.www.mybaits.sqlsession.defaults;

import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.confiuration.Mapper;
import com.baiye.www.mybaits.datasource.DataSourceFactory;
import com.baiye.www.mybaits.datasource.unpooled.UnpooledDataSource;
import com.baiye.www.mybaits.datasource.unpooled.UnpooledDataSourceFactory;
import com.baiye.www.mybaits.executor.Executor;
import com.baiye.www.mybaits.executor.SimpleExecutor;
import com.baiye.www.mybaits.proxy.MapperProxy;
import com.baiye.www.mybaits.sqlsession.SqlSession;
import com.baiye.www.utils.ConnectionUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/07/11:22
 * @Description:
 */
public class DefaultSqlSession implements SqlSession {
    protected Configuration configuration;
    private Executor executor;
    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        executor = new SimpleExecutor(configuration);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T getMapper(Class<T> daoInterfaceClass) {
        return (T) Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(),new Class[]{daoInterfaceClass},new MapperProxy(this));
    }



    @Override
    public <T> T selectOne(String mapperName, Object parameter) {
        List<T> selectList = this.selectList(mapperName,parameter);
        if(selectList == null || selectList.size() == 0){
            return null;
        }
        if(selectList.size() == 1){
            return (T) selectList.get(0);
        }else{
            throw new RuntimeException("too many result");
        }
    }

    @Override
    public <T> List<T> selectList(String mapperName, Object parameter) {
        return executor.query(configuration.getMappers().get(mapperName),parameter);
    }

    @Override
    public int insert(String mapperName, Object parameter) {
        return executor.update(configuration.getMappers().get(mapperName),parameter);
    }

    @Override
    public int update(String mapperName, Object parameter) {
        return executor.update(configuration.getMappers().get(mapperName),parameter);
    }

    @Override
    public int delete(String mapperName, Object parameter) {
        return executor.update(configuration.getMappers().get(mapperName),parameter);
    }

    @Override
    public void close() {
        executor.close();
    }
}
