package com.baiye.www.mybaits.sqlsession;

import com.baiye.www.mybaits.confiuration.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/07/11:22
 * @Description:
 */
public interface SqlSession {
    <T> T getMapper(Class<T> daoInterfaceClass);

    <T> T selectOne(String mapperName, Object[] parameter);
    <T> List<T> selectList(String mapperName, Object[] parameter);
    int insert(String mapperName,Object[] parameter);
    int update(String mapperName,Object[] parameter);
    int delete(String mapperName,Object[] parameter);
    void clearCache();
    void close();
}
