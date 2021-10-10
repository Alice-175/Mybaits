package com.baiye.www.mybaits.transaction.jdbc;

import com.baiye.www.mybaits.transaction.Transaction;
import com.baiye.www.mybaits.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/10/9 23:01
 */
public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection conn,boolean autoComm) {
        return new JdbcTransaction(conn,autoComm);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource,boolean autoComm) {
        return new JdbcTransaction(dataSource,null,autoComm);
    }
}
