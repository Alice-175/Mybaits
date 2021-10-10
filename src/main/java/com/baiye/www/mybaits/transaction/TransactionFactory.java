package com.baiye.www.mybaits.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author GUOZHIPENG
 * @version 1.0
 * @date 2021/8/14 16:59
 */
public interface TransactionFactory {

    Transaction newTransaction(Connection conn, boolean autoCommit);

    Transaction newTransaction(DataSource dataSource, boolean autoCommit);

}
