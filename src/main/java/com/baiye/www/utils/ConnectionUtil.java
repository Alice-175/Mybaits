package com.baiye.www.utils;

import com.baiye.www.mybaits.confiuration.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/13/16:37
 * @Description:
 */
public class ConnectionUtil {


    public static Connection getConnection(Configuration configuration) {
        try {
            Class.forName(configuration.getDriver());
            return DriverManager.getConnection(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
