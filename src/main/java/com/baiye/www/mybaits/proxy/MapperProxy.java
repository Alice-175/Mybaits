package com.baiye.www.mybaits.proxy;

import com.baiye.www.mybaits.confiuration.Mapper;
import com.baiye.www.mybaits.executor.Executor;
import com.baiye.www.mybaits.executor.SimpleExecutor;
import com.baiye.www.mybaits.sqlsession.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/13/16:54
 * @Description:
 */
public class MapperProxy implements InvocationHandler {
    //map的key是全限定类名+方法名
    //private Map<String, Mapper> mappers;
    private SqlSession sqlSession;


    public MapperProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getDeclaringClass().getName()+"."+method.getName());
        //最终还是将执行方法转给 sqlSession，因为 sqlSession 里面封装了 Executor
        //根据调用方法的类名和方法名以及参数，传给 sqlSession 对应的方法
        System.out.println("method.getReturnType()="+method.getReturnType());
        if(Collection.class.isAssignableFrom(method.getReturnType())){
            return sqlSession.selectList(method.getDeclaringClass().getName()+"."+method.getName(),args==null?null:args[0]);
        }else if(void.class.isAssignableFrom(method.getReturnType())){
            return sqlSession.update(method.getDeclaringClass().getName()+"."+method.getName(),args==null?null:args[0]);
        }else{
            return sqlSession.selectOne(method.getDeclaringClass().getName()+"."+method.getName(),args==null?null:args[0]);
        }
//        String className = method.getDeclaringClass().getName();
//        String key = className+"."+method.getName();
//        sqlSession.get
//        Mapper mapper = mappers.get(key);
//        if(mapper == null){
//            throw new IllegalArgumentException("传入的参数有误");
//        }
//        //6.调用工具类执行查询所有
//        return new SimpleExecutor().selectList(mapper,args);
    }
}
