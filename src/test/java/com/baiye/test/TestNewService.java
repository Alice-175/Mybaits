package com.baiye.test;

import java.io.IOException;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/8/12 12:18
 */
public class TestNewService {

 //   SimpleExecutor simpleExecutor = new SimpleExecutor();

    private void findUser(Object[] o) throws IOException {


        //---------简化（实际代码）----------

        //mapperName和数据
//        List<Object> u = simpleExecutor.query("findById", o);
        // System.out.println(u.toString());






        //---------原型(不需要写)-----
//        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
//        Configuration configuration = XMLConfigBuilder.loadConfiguration(in);
//        SimpleExecutor simpleExecutor = new SimpleExecutor(configuration);
//        //sqlXML里的方法名
//        String name = "com.baiye.www.dao.IUserDao.findById";
//        Mapper mapper = configuration.getMappers().get(name);
//        List<Object> u = simpleExecutor.query(mapper, o);
//        System.out.println(u.toString());



    }
}
