package com.baiye.www.mybaits.executor;

import com.baiye.www.mybaits.confiuration.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/17/8:36
 * @Description:
 */
public interface Executor {
    <E> List<E> query(Mapper mapper, Object[] parameter);

    void close();

    int update(Mapper mapper, Object[] parameter);
}
