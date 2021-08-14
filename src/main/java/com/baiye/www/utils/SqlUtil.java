package com.baiye.www.utils;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/8/8 20:30
 */
public class SqlUtil {

    public static String paramToSql(String originalSql, Object[] object) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
        int paramCount = StringUtil.getTargetStringNum(originalSql, "#");
        //没有参数
        if (paramCount < 1) {
            return originalSql;
        } else if (paramCount == 1) { //只有一个参数即只有一个基本数据类型,转化为字符串
            String regex = "#\\{([^}])*}";
            //将 sql 语句中的 #{*} 替换为 ？
            return originalSql.replaceAll(regex, "\"" + object[0] + "" + "\"");
        } else if (paramCount > 1 && object.length == 1) { //多个参数，且传实体对象
            Class obj = object[0].getClass();
            Field[] declaredFields = obj.getDeclaredFields();
            for (Field field : declaredFields) {
                String fieldName = field.getName();
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, obj);
                Method readMethod = propertyDescriptor.getReadMethod();
                Object o = readMethod.invoke(object[0]);
                originalSql = originalSql.replace("#{" + fieldName + "}", "\"" + o + "" + "\"");
            }
            return originalSql;
        } else { //多个参数，传的也是多个基本类型
            for (Object o : object) {
                HashMap<String, String> map = (HashMap<String, String>) o;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String regex = "#\\{" + entry.getKey() + "}";
                    //将 sql 语句中的 #{*} 替换为 ？
                    originalSql = originalSql.replaceAll(regex, "\"" + entry.getValue() + "" + "\"");
                }
            }
            return originalSql;
        }
    }

}
