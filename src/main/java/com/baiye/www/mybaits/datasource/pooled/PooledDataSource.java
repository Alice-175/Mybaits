package com.baiye.www.mybaits.datasource.pooled;

import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.datasource.unpooled.UnpooledDataSource;

import javax.sql.DataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/14/15:28
 * @Description:
 */
public class PooledDataSource implements DataSource {
    private final UnpooledDataSource dataSource;
    private PoolState poolState;

    protected int poolMaximumActiveConnections = 10;
    protected int poolMaximumIdleConnections = 5;
    protected int poolMaximumCheckoutTime = 20000;
    protected int poolTimeToWait = 20000;
    protected int poolMaximumLocalBadConnectionTolerance = 3;
    protected String poolPingQuery = "NO PING QUERY SET";
    protected boolean poolPingEnabled;
    protected int poolPingConnectionsNotUsedFor;

    private int expectedConnectionTypeCode;

    public PooledDataSource() {
        this.dataSource = new UnpooledDataSource();
    }
    public PooledDataSource(UnpooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public PooledDataSource(Configuration config) {

        this.dataSource = new UnpooledDataSource(config);
        try {
            Connection conn = dataSource.getConnection();
            PooledConnection pooledConnection = new PooledConnection(conn,this);
            this.poolState = new PoolState();
            for(int i=0;i<poolMaximumIdleConnections;i++){
                poolState.idleConnections.add(pooledConnection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public PooledConnection popConnection(String username, String password) throws SQLException {
        PooledConnection con = null;
        while (con == null){
            synchronized (poolState){
                if(poolState.idleConnections.size()>0){
                    con = poolState.idleConnections.remove(0);
                }else {
                    if (poolState.activeConnections.size() < poolMaximumActiveConnections){
                        Connection conn = dataSource.getConnection();
                        con = new PooledConnection(conn,this);

                    }else{
                        PooledConnection oldestActiveConnection = poolState.activeConnections.get(0);
                        if(oldestActiveConnection.isValid()){
                            poolState.activeConnections.remove(oldestActiveConnection);
                        }
                    }

                }
            }
        }
        poolState.activeConnections.add(con);
        return con;
    }
    protected void pushConnection(PooledConnection pooledConnection) {
        synchronized (poolState) {
            poolState.activeConnections.remove(pooledConnection);
            if(pooledConnection.isValid()){
                if (poolState.idleConnections.size() < poolMaximumIdleConnections){
                    PooledConnection newConn = new PooledConnection(pooledConnection.getRealConnection(), this);
                    poolState.idleConnections.add(newConn);

                }else {
                    try {
                        pooledConnection.getRealConnection().close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
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
