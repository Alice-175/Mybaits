package com.baiye.www.mybaits.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/8/14 15:19
 */
class PooledConnection implements InvocationHandler {
    private static final String CLOSE = "close";
    private static final Class<?>[] IFACES = new Class<?>[] { Connection.class };

    private final int hashCode;
    private final PooledDataSource dataSource;
    private final Connection realConnection;
    private final Connection proxyConnection;
    private long checkoutTimestamp;
    private long createdTimestamp;
    private long lastUsedTimestamp;
    private int connectionTypeCode;
    private boolean valid;
    public PooledConnection(Connection connection, PooledDataSource dataSource) {
        this.hashCode = connection.hashCode();
        this.realConnection = connection;
        this.dataSource = dataSource;
        this.createdTimestamp = System.currentTimeMillis();
        this.lastUsedTimestamp = System.currentTimeMillis();
        this.valid = true;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (CLOSE.equals(methodName)) {
            dataSource.pushConnection(this);
            return null;
        }
        return method.invoke(realConnection, args);
    }

    private boolean validate(Connection conn)
    {
        boolean isValidated = false;
        try {
            return conn.isClosed();
        } catch (SQLException e) {
        }

        return isValidated;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }
    public Connection getRealConnection() {
        return realConnection;
    }

    public boolean isValid() {
        try {
            return valid && realConnection != null && (!realConnection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
