package com.baiye.www.mybaits.datasource.unpooled;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/08/15:07
 * @Description:
 */
public class UnpooledDataSource implements DataSource {
    private String driver;
    private String url;
    private String username;
    private String password;
    private static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<>();
    static {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()){
            Driver driver = drivers.nextElement();
            String driverName = driver.getClass().getName();
            registeredDrivers.put(driverName,driver);
        }
    }

    private Boolean autoCommit;
    private Integer defaultTransactionIsolationLevel;
    private Integer defaultNetworkTimeout;

    public UnpooledDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public UnpooledDataSource() {

    }

    private Connection doGetConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return doGetConnection(username, password);
    }



    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }


    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }


    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }


    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }


    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }


    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
