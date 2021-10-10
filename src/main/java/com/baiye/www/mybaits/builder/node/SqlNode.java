package com.baiye.www.mybaits.builder.node;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/8/31 16:00
 */

import org.dom4j.Element;

import java.util.Map;

public class SqlNode extends BaseNode {

    @Override
    public boolean parse(Map<String, Object> currParams, Map<String, Object> globalParams, Element ele, StringBuilder sb) throws Exception {
        return true;
    }

}
