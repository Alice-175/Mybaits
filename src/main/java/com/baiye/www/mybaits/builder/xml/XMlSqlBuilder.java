package com.baiye.www.mybaits.builder.xml;

import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Iterator;
import java.util.List;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/9/1 10:56
 */
public class XMlSqlBuilder {
    public static String sqlParser(Element element, Object[] object) {
        StringBuilder sb=new StringBuilder();
        List<Node> nodes = element.selectNodes("node()");
        for(Node n:nodes){
            if(n.getNodeTypeName()=="Text"){
                sb.append(n);
            }else {
                String str = n.getName();
                sb.append(str);
            }

        }
        return sb.toString();
    }
}
