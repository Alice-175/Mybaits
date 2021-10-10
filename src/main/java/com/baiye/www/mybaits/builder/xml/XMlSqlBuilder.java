package com.baiye.www.mybaits.builder.xml;

import ognl.Ognl;
import ognl.OgnlException;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/9/1 10:56
 */
public class XMlSqlBuilder {
    public static String sqlParser(Element element, Object[] object) {
        StringBuilder sb = new StringBuilder();
        List<Node> nodes = element.selectNodes("node()");
        for (Node n : nodes) {
            if (n.getNodeTypeName() == "Text") {
                sb.append(n.getText());
            } else {
                try {
                    sb.append(OgnlParser((Element) n, object));
                } catch (OgnlException e) {
                    e.printStackTrace();
                }
            }

        }
        return sb.toString();
    }

    public static String OgnlParser(Element e, Object[] object) throws OgnlException {
        StringBuilder resultStr = new StringBuilder();
        String str = e.getName();
        if (str.equals("if")) {
            String attribute = e.attributeValue("test");
            boolean result = (boolean) Ognl.getValue(attribute, object[0]);
            if (result) {
                resultStr.append(e.getText());
            }

        } else if (str.equals("foreach")) {

        } else if (str.equals("where")) {
            List<Element> elements = e.elements();
            StringBuilder tempSb = new StringBuilder();
            for (Element ele : elements) {
                tempSb.append(OgnlParser(ele, object));
            }
            String temp = tempSb.toString().trim();
            if (temp.length() > 0) {
                resultStr.append(" where ");
                if (temp.indexOf("AND") == 0 || temp.indexOf("and") == 0 || temp.indexOf("OR") == 0 || temp.indexOf("or") == 0) {
                    temp.substring(0, 3);
                }
                if (temp.indexOf("OR") == 0 || temp.indexOf("or") == 0) {
                    temp.substring(0, 2);
                }
                resultStr.append(" " + temp);
            }
        }
        return resultStr.toString();

    }
}
