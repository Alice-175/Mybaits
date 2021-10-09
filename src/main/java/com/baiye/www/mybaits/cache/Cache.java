package com.baiye.www.mybaits.cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/10/9 9:36
 */
public interface Cache {
    String getId();


    void putObject(Object key, Object value);


    Object getObject(Object key);


    Object removeObject(Object key);

    /**
     * Clears this cache instance.
     */
    void clear();

    int getSize();


    default ReadWriteLock getReadWriteLock() {
        return null;
    }
}
