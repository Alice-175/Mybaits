package com.baiye.www.mybaits.confiuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/06/10:03
 * @Description:
 */
public class Mapper {

    private String sql;
    private String resultType;
    private String parameterType;
    private String resultMap;

    public Mapper(String sql, String resultType) {
        this.sql = sql;
        this.resultType = resultType;
    }

    public Mapper(String sql, String resultType, String parameterType, String resultMap) {
        this.sql = sql;
        this.resultType = resultType;
        this.parameterType = parameterType;
        this.resultMap = resultMap;
    }

    public Mapper() {

    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }
}
