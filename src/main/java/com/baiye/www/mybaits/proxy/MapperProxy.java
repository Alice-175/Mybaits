package com.baiye.www.mybaits.proxy;

import com.baiye.www.mybaits.annotation.Param;
import com.baiye.www.mybaits.confiuration.Mapper;
import com.baiye.www.mybaits.executor.Executor;
import com.baiye.www.mybaits.executor.SimpleExecutor;
import com.baiye.www.mybaits.sqlsession.SqlSession;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;

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

        if(args != null&&args.length > 1){
            Annotation[][] annotation = method.getParameterAnnotations();
            for(int i = 0;i<args.length;i++){
                Map<String, String> map = new HashMap(1);
                Param anno = (Param)annotation[i][0];
                map.put(anno.value(),args[i]+"");
                args[i] =map;
            }
        }
        if(Collection.class.isAssignableFrom(method.getReturnType())){
            return sqlSession.selectList(method.getDeclaringClass().getName()+"."+method.getName(), args);
        }else if(void.class.isAssignableFrom(method.getReturnType())){
            return sqlSession.update(method.getDeclaringClass().getName()+"."+method.getName(), args);
        }else{
            return sqlSession.selectOne(method.getDeclaringClass().getName()+"."+method.getName(), args);
        }

    }
}
