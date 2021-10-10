package com.baiye.www.mybaits.executor;

import com.baiye.www.exceptions.MybatisException;
import com.baiye.www.mybaits.builder.xml.XMlSqlBuilder;
import com.baiye.www.mybaits.cache.PerpetualCache;
import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.confiuration.Mapper;
import com.baiye.www.mybaits.io.Resources;
import com.baiye.www.mybaits.transaction.Transaction;
import com.baiye.www.mybaits.transaction.jdbc.JdbcTransactionFactory;
import com.baiye.www.utils.SqlUtil;
import com.baiye.www.utils.StringUtil;
import com.baiye.www.mybaits.builder.xml.XMLConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
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
    protected Transaction transaction;
    private boolean closed;
    protected PerpetualCache localCache =new PerpetualCache();
    private boolean autoCommit;

    Logger logger = LoggerFactory.getLogger(SqlUtil.class);

    private final Configuration configuration;
    private Connection conn;


//    public SimpleExecutor(Configuration configuration,,boolean autoCommit){
//        this("SqlMapConfig.xml",autoCommit);
//        this.configuration = configuration;
//    }
//    public SimpleExecutor(String resourceName,boolean autoCommit) {
//        this.autoCommit=autoCommit;
//        InputStream in = null;
//        try {
//            in = Resources.getResourceAsStream(resourceName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        configuration = XMLConfigBuilder.loadConfiguration(in);
//        this.transaction=new JdbcTransactionFactory().newTransaction(configuration.getEnvironment().getDataSource(),autoCommit);
//        try {
//            this.conn=this.transaction.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//public SimpleExecutor(Configuration configuration, Transaction transaction) {
//    super(configuration, transaction);
//}
    public SimpleExecutor(Configuration configuration,boolean autoCommit) {
        this.configuration = configuration;
        this.autoCommit=autoCommit;
        this.transaction=new JdbcTransactionFactory().newTransaction(configuration.getEnvironment().getDataSource(),autoCommit);
        try {
            this.conn=this.transaction.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <E> List<E> query(Mapper mapper,Object[] object) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String originalSql = mapper.getSql();
        String resultType = mapper.getResultType();
        String parameterType = mapper.getParameterType();

        String resultSql = null;
        logger.info("originalSql = "+originalSql);
        try {
            if(mapper.getElement().elements().size()>0){
                originalSql = XMlSqlBuilder.sqlParser(mapper.getElement(),object);
            }
            resultSql = SqlUtil.paramToSql(originalSql,object);
            logger.info("resultSql = "+resultSql);
            List<E> cache= (List<E>) localCache.getObject(resultSql);
            if(cache!=null){
                return cache;
            }
            if(configuration.isEnableCache()){
                List<E> globalCache= (List<E>) configuration.getPerpetualCache().getObject(resultSql);
                if(globalCache!=null){
                    return globalCache;
                }
            }
            preparedStatement = conn.prepareStatement(resultSql);
            Class pojoClass = Class.forName(resultType);
            resultSet = preparedStatement.executeQuery();
            List<E> list = new ArrayList<>();
            while (resultSet.next()){
                E pojo = (E) pojoClass.newInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for(int i=1;i<=metaData.getColumnCount();i++){
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(columnName);
                    columnName = StringUtil.underlineToHump(columnName);

                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, pojoClass);
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(pojo,value);
                }
                list.add(pojo);
            }
            localCache.putObject(resultSql,list);
            if(configuration.isEnableCache()){
                configuration.getPerpetualCache().putObject(resultSql,list);
            }
            return list;
        } catch (InvocationTargetException | IllegalAccessException | IntrospectionException | SQLException | InstantiationException | ClassNotFoundException e) {
            throw new MybatisException("query时sql执行或注入实体类错误",e);
        }finally {
            release(conn,preparedStatement,resultSet);
        }


    }
    public <E> List<E> query(String mapperName, Object[] object) {
        Mapper mapper = configuration.getMappers().get(mapperName);
        return query(mapper,object);
    }



    @Override
    public int update(Mapper mapper, Object[] object) {
        localCache.clear();
        if(configuration.isEnableCache()){
            configuration.clearPerpetualCache();
        }

        PreparedStatement preparedStatement = null;
        //xml里的sql字符串
        String originalSql = mapper.getSql();
        String resultSql = null;
        String parameterType = mapper.getParameterType();
        logger.info("originalSql = "+originalSql);
        try {
            resultSql = SqlUtil.paramToSql(originalSql,object);
            logger.info("resultSql = "+resultSql);
            preparedStatement = conn.prepareStatement(resultSql);
            return preparedStatement.executeUpdate();
        } catch (SQLException | IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new MybatisException("update时sql执行或注入实体类错误",e);
        }finally {

            release(conn,preparedStatement,null);
        }
    }
    public int update(String mapperName, Object[] object) {
        Mapper mapper = configuration.getMappers().get(mapperName);
        return update(mapper,object);
    }

    private void release(Connection con,PreparedStatement pstm,ResultSet rs){
        if(con != null){
            try {
                if(transaction.isAutoCommit()){
                    transaction.close();
                }

            }catch(Exception e){
                throw new MybatisException("资源释放异常",e);
            }
        }
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
    @Override
    public void close() {
        localCache.clear();
        try {
            transaction.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void clearLocalCache() {
        if (!closed) {
            localCache.clear();
        }
    }

    @Override
    public void rollback() {
        try {
            transaction.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() {
        try {
            transaction.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
