package com.baiye.www.mybaits.builder.node;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/8/31 15:58
 */
import org.dom4j.Element;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象节点
 * @author rongdi
 */
public abstract class BaseNode {
    public abstract boolean parse(Map<String, Object> currParams, Map<String, Object> globalParams, Element ele,StringBuilder sb) throws Exception;
    public void pre(Map<String, Object> currParams,Map<String, Object> globalParams,Element ele,StringBuilder sb) throws Exception {
    }
    public void after(Map<String, Object> currParams,Map<String, Object> globalParams,Element ele,StringBuilder sb) throws Exception {
    }
    protected Map<String, Object> getAllParams(Map<String, Object> currParams,
                                               Map<String, Object> globalParams) {
        Map<String,Object> allParams = new HashMap<String,Object>();
        allParams.putAll(globalParams);
        allParams.putAll(currParams);
        return allParams;
    }
}