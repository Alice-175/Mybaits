package com.baiye.www.mybaits.executor;

import com.baiye.www.exceptions.MybatisException;
import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.confiuration.Mapper;
import com.baiye.www.utils.StringUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/13/17:08
 * @Description:
 */
public class SimpleExecutor implements Executor{
    private final Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        this.configuration = configuration;
    }
    @Override
    public <E> List<E> query(Mapper mapper,Object object) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String tempSql = mapper.getSql();
        String resultType = mapper.getResultType();
        String parameterType = mapper.getParameterType();
        //没有参数
        String  sql = null;
        try {
        Connection conn = DriverManager.getConnection(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());

        if(object == null){
            sql = tempSql;
            preparedStatement = conn.prepareStatement(sql);

        }else {//只有一个参数,转化为字符串
            //String param = objects[0]+"";
            String regex = "#\\{([^}])*\\}";
            // 将 sql 语句中的 #{userId} 替换为 ？
            sql = tempSql.replaceAll(regex,"?");
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1,object);
            System.out.println(sql+object.toString());
           // System.out.println(preparedStatement.get);
        }




            Class pojoClass = Class.forName(resultType);

            resultSet = preparedStatement.executeQuery();

            List<E> list = new ArrayList<>();
            while (resultSet.next()){
                E pojo = (E) pojoClass.newInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for(int i=1;i<=metaData.getColumnCount();i++){
                    String columnName = metaData.getColumnName(i);
                    columnName = StringUtil.underlineToHump(columnName);
                    Object value = resultSet.getObject(columnName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, pojoClass);
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(pojo,value);
                }
                list.add(pojo);
            }
            return list;
        } catch (Exception e) {
            throw new MybatisException("query时sql执行或注入实体类错误",e);

        }finally {
            release(preparedStatement,resultSet);
        }


    }

    @Override
    public void close() {

    }

    @Override
    public int update(Mapper mapper, Object object) {
        PreparedStatement preparedStatement = null;

        String originalSql = mapper.getSql();
        String realSql = null;
        String parameterType = mapper.getParameterType();
        //没有参数
        String  sql = null;
        String temp = originalSql.replace("#","");
        int paramCount = originalSql.length()-temp.length();
        int resultInt = -1;
        try {
            Connection conn = DriverManager.getConnection(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());

            if(object == null){
                realSql = originalSql;
                preparedStatement = conn.prepareStatement(realSql);

            }else if(paramCount == 1){//只有一个参数即只有一个基本数据类型,转化为字符串,若只有一个参数，则传基本类型，若有多个，封装成pojo
                //String param = objects[0]+"";
                String regex = "#\\{([^}])*\\}";
                // 将 sql 语句中的 #{userId} 替换为 ？
                realSql = originalSql.replaceAll(regex,"?");
                preparedStatement = conn.prepareStatement(realSql);
                preparedStatement.setObject(1,object);


            }else{//实体类型参数
                Class obj = object.getClass();
                Field[] declaredFields = obj.getDeclaredFields();
                for (Field field:declaredFields) {
                    String fieldName = field.getName();
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, obj);
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Object o = readMethod.invoke(object);
                    originalSql = originalSql.replace("#{"+fieldName+"}","\""+o+""+"\"");
                }
                realSql = originalSql;
                System.out.println(realSql);
                preparedStatement = conn.prepareStatement(realSql);

            }

            resultInt= preparedStatement.executeUpdate();

            return resultInt;
        } catch (Exception e) {
            throw new MybatisException("update时sql执行或注入实体类错误",e);
        }finally {
            release(preparedStatement,null);
        }
    }

    private void release(PreparedStatement pstm,ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            }catch(Exception e){
                throw new MybatisException("资源释放异常",e);
            }
        }

        if(pstm != null){
            try {
                pstm.close();
            }catch(Exception e){
                throw new MybatisException("资源释放异常",e);
            }
        }
    }



}
