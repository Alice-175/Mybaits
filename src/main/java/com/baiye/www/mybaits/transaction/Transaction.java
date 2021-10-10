package com.baiye.www.mybaits.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/10/9 22:35
 */
public interface Transaction {

    Connection getConnection() throws SQLException;


    void commit() throws SQLException;


    void rollback() throws SQLException;


    void close() throws SQLException;

    boolean isAutoCommit() throws SQLException;

    void setAutoCommit(boolean autoCommit);

}
