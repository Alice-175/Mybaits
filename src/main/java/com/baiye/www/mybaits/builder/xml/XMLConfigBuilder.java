package com.baiye.www.mybaits.builder.xml;

import com.baiye.www.mybaits.annotation.Select;
import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.confiuration.Mapper;
import com.baiye.www.mybaits.datasource.pooled.PooledDataSourceFactory;
import com.baiye.www.mybaits.io.Resources;
import com.baiye.www.mybaits.mapping.Environment;
import com.sun.jndi.ldap.pool.PooledConnectionFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/05/16:01
 * @Description:
 */
public class XMLConfigBuilder {


    public static Configuration loadConfiguration(InputStream in) {
        Configuration config=new Configuration();
        SAXReader saxReader=new SAXReader();
        try {
            Document document=saxReader.read(in);
            Element root = document.getRootElement();
            //xpath '//'表示在当先所选节点查找
            List dataSourceNode = root.selectNodes("//dataSource");
            String type = ((Element)dataSourceNode.get(0)).attributeValue("type");
            config.setDataSourceType(type);
            List nodes = root.selectNodes("//property");
            Iterator iterator=nodes.iterator();
            while (iterator.hasNext()){
                Element element = (Element) iterator.next();
                String name = element.attributeValue("name");
                String value = element.attributeValue("value");
                if("driver".equals(name)){
                    config.setDriver(value);
                }else if ("url".equals(name)){
                    config.setUrl(value);
                }else if ("username".equals(name)){
                    config.setUsername(value);
                }else if ("password".equals(name)){
                    config.setPassword(value);
                }else{
                    throw new RuntimeException("xml sql config error!");
                }
            }

            //mappers
            List mapperList = root.selectNodes("//mappers/mapper");
            Iterator mapperIterator = mapperList.iterator();
            Map<String, Mapper> mappers=new HashMap<>();
            while (mapperIterator.hasNext()){
                Element element = (Element) mapperIterator.next();
                Attribute attribute = element.attribute("resource");
                if(attribute != null){
                    //使用xml
                    String classPath=attribute.getValue();
                    Map<String, Mapper> mapper = loadMapperXMLConfiguration(classPath);
                    mappers.putAll(mapper);
                }else if(element.attribute("class") != null){
                    //使用注解
                    String classPath=element.attribute("class").getValue();
                    Map<String, Mapper> mapper = loadMapperAnnotation(classPath);
                    mappers.putAll(mapper);
                }else {
                    throw new RuntimeException("xml mapper config error");
                }
            }
            config.setMappers(mappers);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("loadConfiguration error!");
        }

        if(config.getDataSourceType().equals("POOLED")){
            config.setEnvironment(new Environment(null,null,new PooledDataSourceFactory(config).getDataSource()));
        }
        return config;
    }

    /**
     * 注解配置 根据传入的全限定类名封装mapper(sql,resultType)
     * @param classPath
     * @return
     */
    private static Map<String,Mapper> loadMapperAnnotation(String classPath) {
        Map<String,Mapper> mappers = new HashMap<String, Mapper>();
        Mapper mapper = new Mapper();
        try {
            Class daoClass = Class.forName(classPath);
            Method[] methods = daoClass.getMethods();
            for (Method method:methods) {
                if(method.isAnnotationPresent(Select.class)){
                    Select selectAnnotation = method.getAnnotation(Select.class);
                    String sql = selectAnnotation.value();
                    mapper.setSql(sql);
                    String methodName = method.getName();
                    Type type = method.getGenericReturnType();
                    //判断type是不是参数化的类型
                    if(type instanceof ParameterizedType){
                        ParameterizedType ptype = (ParameterizedType)type;
                        Type[] types = ptype.getActualTypeArguments();
                        Class domainClass = (Class)types[0];
                        //获取domainClass的类名
                        String resultType = domainClass.getName();
                        mapper.setResultType(resultType);
                    }
                    String key = classPath+"."+methodName;
                    mappers.put(key,mapper);
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mappers;
    }


    /**
     * xml配置 根据传入的全限定类名封装mapper(sql,resultType)
     * @param xmlPath
     * @return
     */
    private static Map<String,Mapper> loadMapperXMLConfiguration(String xmlPath) {
        Map<String,Mapper> mappers = new HashMap<String, Mapper>();
        InputStream in = null;
        try {
            in  = Resources.getResourceAsStream(xmlPath);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);
            Element root = document.getRootElement();
            String namespace = root.attributeValue("namespace");
            List selectNodes = root.selectNodes("//select");
            List insertNodes = root.selectNodes("//insert");
            List updateNodes = root.selectNodes("//update");
            List deleteNodes = root.selectNodes("//delete");
            selectNodes.addAll(insertNodes);
            selectNodes.addAll(updateNodes);
            selectNodes.addAll(deleteNodes);

            Iterator iterator = selectNodes.iterator();
            while(iterator.hasNext()){
                Element element = (Element) iterator.next();
                String id = element.attributeValue("id");
                String resultType = element.attributeValue("resultType");
                String parameterType = element.attributeValue("parameterType");
                String resultMap = element.attributeValue("resultMap");
                String sql = element.getText();
                String key = namespace+"."+id;
                Mapper mapper=new Mapper(sql,resultType,parameterType,resultMap);
                mappers.put(key, mapper);
            }
            in.close();


        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        return mappers;
    }
}
