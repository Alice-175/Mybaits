package com.baiye.test;

import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.confiuration.Mapper;
import com.baiye.www.mybaits.executor.SimpleExecutor;
import com.baiye.www.mybaits.io.Resources;
import com.baiye.www.mybaits.builder.xml.XMLConfigBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/8/12 10:55
 */
public class TempTest {
    public static void main(String[] args) throws IOException {
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        Configuration configuration = XMLConfigBuilder.loadConfiguration(in);
        SimpleExecutor simpleExecutor = new SimpleExecutor(configuration);
        Object[] o = new Object[1];
        o[0]=41;
        //sqlXML里的方法名
        String name = "com.baiye.www.dao.IUserDao.findById";
        Mapper mapper = configuration.getMappers().get(name);
        List<Object> u = simpleExecutor.query(mapper, o);
        System.out.println(u.toString());
    }

}
