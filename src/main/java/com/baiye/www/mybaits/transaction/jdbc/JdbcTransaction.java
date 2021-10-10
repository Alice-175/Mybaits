package com.baiye.www.mybaits.transaction.jdbc;

import com.baiye.www.mybaits.transaction.Transaction;
import com.baiye.www.mybaits.transaction.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/10/9 22:40
 */
public class JdbcTransaction implements Transaction {
    protected Connection connection;
    protected DataSource dataSource;
    protected TransactionIsolationLevel level;
    protected boolean autoCommit;

    public JdbcTransaction(Connection conn,boolean autoComm) {
        this.connection=conn;
        this.autoCommit=autoComm;
    }

    public JdbcTransaction(DataSource dataSource) {
        this.dataSource=dataSource;
    }
    public JdbcTransaction(DataSource ds, TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit) {
        dataSource = ds;
        level = desiredLevel;
        autoCommit = desiredAutoCommit;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            openConnection();
        }
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            /**若是返回连接池需重置*/
            resetAutoCommit();
            connection.close();
        }
    }
    protected void resetAutoCommit() {
        try {
            if (!connection.getAutoCommit()) {

                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    protected void openConnection() throws SQLException {
        connection = dataSource.getConnection();
        if (level != null) {
            connection.setTransactionIsolation(level.getLevel());
        }
        setDesiredAutoCommit(autoCommit);
    }
    protected void setDesiredAutoCommit(boolean desiredAutoCommit) {
        try {
            if (connection.getAutoCommit() != desiredAutoCommit) {
                connection.setAutoCommit(desiredAutoCommit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TransactionIsolationLevel getLevel() {
        return level;
    }

    public void setLevel(TransactionIsolationLevel level) {
        this.level = level;
    }

    @Override
    public boolean isAutoCommit() {
        return autoCommit;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
