package com.baiye.www.mybaits.confiuration;

import org.dom4j.Element;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/06/10:03
 * @Description:
 */
public class Mapper {

    private String sql;
    private Element element;
    private String resultType;
    private String parameterType;
    private String resultMap;


    public Mapper(String sql, Element element, String resultType, String parameterType, String resultMap) {
        this.sql = sql;
        this.element = element;
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

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
