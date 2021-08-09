package com.baiye.www.mybaits.executor;

import com.baiye.www.exceptions.MybatisException;
import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.confiuration.Mapper;
import com.baiye.www.utils.SqlUtil;
import com.baiye.www.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    Logger logger = LoggerFactory.getLogger(SqlUtil.class);

    private final Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        this.configuration = configuration;
    }
    @Override
    public <E> List<E> query(Mapper mapper,Object[] object) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String originalSql = mapper.getSql();
        String resultType = mapper.getResultType();
        String parameterType = mapper.getParameterType();

        String resultSql = null;
        Connection conn = null;
        logger.info("originalSql = "+originalSql);
        try {
            resultSql = SqlUtil.paramToSql(originalSql,object);
            logger.info("resultSql = "+resultSql);
            conn = DriverManager.getConnection(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());
            preparedStatement = conn.prepareStatement(resultSql);
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
        } catch (InvocationTargetException | IllegalAccessException | IntrospectionException | SQLException | InstantiationException | ClassNotFoundException e) {
            throw new MybatisException("query时sql执行或注入实体类错误",e);
        }finally {
            release(preparedStatement,resultSet);
        }


    }

    @Override
    public void close() {

    }

    @Override
    public int update(Mapper mapper, Object[] object) {
        PreparedStatement preparedStatement = null;
        //xml里的sql字符串
        String originalSql = mapper.getSql();
        String resultSql = null;
        String parameterType = mapper.getParameterType();
        Connection conn = null;
        logger.info("originalSql = "+originalSql);
        try {
            resultSql = SqlUtil.paramToSql(originalSql,object);
            logger.info("resultSql = "+resultSql);
            conn = DriverManager.getConnection(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());
            preparedStatement = conn.prepareStatement(resultSql);
            return preparedStatement.executeUpdate();
        } catch (SQLException | IntrospectionException | IllegalAccessException | InvocationTargetException e) {
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
