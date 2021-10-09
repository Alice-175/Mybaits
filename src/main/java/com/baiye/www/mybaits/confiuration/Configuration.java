package com.baiye.www.mybaits.confiuration;

import com.baiye.www.mybaits.cache.Cache;
import com.baiye.www.mybaits.cache.PerpetualCache;
import com.baiye.www.mybaits.mapping.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/06/10:43
 * @Description:
 */
public class Configuration {
    protected Environment environment;
    private String dataSourceType;
    private String driver;
    private String url;
    private String username;
    private String password;
    private Cache perpetualCache=new PerpetualCache();
    private boolean enableCache;
    //全限定类名+方法名，mapper
    private Map<String,Mapper> mappers=new HashMap<String,Mapper>();

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Mapper> getMappers() {
        return mappers;
    }

    public void setMappers(Map<String, Mapper> mappers) {
        this.mappers = mappers;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Cache getPerpetualCache() {
        return perpetualCache;
    }

    public void clearPerpetualCache() {
        this.perpetualCache.clear();
    }

    public boolean isEnableCache() {
        return enableCache;
    }

    public void cleanEnableCache(boolean enableCache) {
        this.enableCache = enableCache;
    }
}
