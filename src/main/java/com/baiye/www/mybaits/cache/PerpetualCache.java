package com.baiye.www.mybaits.cache;


import java.util.HashMap;
import java.util.Map;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/10/9 9:36
 */
public class PerpetualCache implements Cache {

    private final Map<Object, Object> cache = new HashMap<>();
    @Override
    public String getId() {
        return null;
    }

    @Override
    public int getSize() {
        return cache.size();
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return cache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cache)) {
            return false;
        }

        Cache otherCache = (Cache) o;
        return getId().equals(otherCache.getId());
    }


}
