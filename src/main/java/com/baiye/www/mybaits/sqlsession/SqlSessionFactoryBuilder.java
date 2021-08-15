package com.baiye.www.mybaits.sqlsession;

import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.sqlsession.defaults.DefaultSqlSessionFactory;
import com.baiye.www.mybaits.builder.xml.XMLConfigBuilder;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/06/11:05
 * @Description:
 */
public class SqlSessionFactoryBuilder {
    private SqlSessionFactory build(InputStream inputStream, String environment, Properties properties){
        Configuration configuration = XMLConfigBuilder.loadConfiguration(inputStream);
        return build(configuration);
    }
    public SqlSessionFactory build(InputStream inputStream){
        return build(inputStream,null,null);
    }
    public SqlSessionFactory build(Configuration configuration){

        return new DefaultSqlSessionFactory(configuration);
    }
}
